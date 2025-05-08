# 분산락을 통한 동시성 제어 보고서

현재 비관락, 낙관락을 적용하여 동시성 제어를 한 로직은 아래와 같습니다.

1. 주문 시 재고 차감
2. 포인트 충전과 사용
3. 쿠폰 발급

해당 기능들은 모두 DB Lock으로 구현되어 있어, DB 부하를 발생시킬 수 있으며,
서버나 DB가 분산되어 있는 환경에서는 일관된 락을 제공할 수 없습니다.

이에 따라서 Redis를 활용하여 분산락을 적용하여 해당 기능들의 동시성 제어를 진행해보도록 하려고 합니다.

---

## AOP를 활용한 분산락

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    String topic();
    
    String key();

    LockType type() default LockType.SIMPLE;

    /** 최대 대기 시간(tryLock) */
    long waitTime() default 5;

    /** 락 자동 만료 시간(leaseTime) */
    long leaseTime() default 30;

    /** 시간 단위 */
    TimeUnit unit() default TimeUnit.SECONDS;
}
```

AOP를 활용하여 분산락을 적용할 예정이므로, Annotation을 구성해줍니다.

topic은 lock의 key에 활용될 시그니처이고, lock을 걸 id를 key로 받아서 최종적인 redis lock key를 구성하게 됩니다.

또한, AOP에서 @Order(Ordered.HIGHEST_PRECEDENCE)를 붙여줌으로 써 Transaction 획득보다 lock을 먼저 획득함으로써 

데이터 무결성을 보장합니다.

```java
@Aspect
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)   // 트랜잭션 AOP 전에 실행
public class DistributedLockAspect {

    private final RedissonClient redisson;

    @Around("@annotation(kr.hhplus.be.server.support.config.redis.DistributedLock)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        DistributedLock ann = method.getAnnotation(DistributedLock.class);

        List<String> keys = parseKeys(ann.topic(), ann.key(), sig.getParameterNames(), pjp.getArgs());

        List<RLock> locks = getLocks(keys, ann);

        return unlockAndProceed(locks, pjp);
    }
    
    // ...
}
```

### 쿠폰 발급 분산락 적용

```java
    @Transactional
    @DistributedLock(topic = "coupon", key = "#criteria.couponId", type = LockType.FAIR)
    public CouponResult.IssueUserCoupon issueUserCoupon(CouponCriteria.IssueUserCoupon criteria) {

        Coupon coupon = couponService.issueValidate(criteria.couponId());
        UserCoupon userCoupon = userCouponService.issue(criteria.toCommand(coupon));
        couponService.deduct(criteria.couponId());

        return CouponResult.IssueUserCoupon.from(userCoupon);
    }
```

해당 로직은 쿠폰을 발급하는 로직으로, 기존에 deduct 메서드에서 발급 시에만 row에 lock을 걸어 동시성 제어를 구현하였지만,

해당 메서드에 lock을 걸 경우, Transaction이 시작된 후에 lock을 획득하게 되고, lock반환보다 transaction을 종료시키게 되어도

발급 전 쿠폰 유효성 검사를 위한 쿠폰 조회가 이루어지기 때문에, JPA의 1차 캐시에 영향으로 의도치 않은 실패 케이스가 발생하여 Facade에 분산락을 적용하였습니다.


### 포인트 충전/사용 분산락 적용

```java
    @DistributedLock(topic = "point", key = "#command.user.id")
    @Transactional
    public Point charge(PointCommand.Charge command) {
        User user = command.user();
        Point point = pointRepository.findByUserIdForUpdate(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원입니다."));
        point.charge(command.amount());

        return point;
    }

    @DistributedLock(topic = "point", key = "#command.user.id")
    @Transactional
    public Point use(PointCommand.Use command) {
        User user = command.user();
        Point point = pointRepository.findByUserIdForUpdate(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원입니다."));

        point.use(command.amount());
        return point;
    }
```

충전과 사용 모두 분산락을 적용하였습니다. 이때, 사용의 경우 '주문' 로직 중간에 진행되기 때문에, AOP에서 Lock을 해제하는 경우

Transaction이 commit 혹은 rollback 이후에 lock을 반환하는 로직을 추가하였습니다.

```java
    private Object unlockAndProceed(List<RLock> locks, ProceedingJoinPoint pjp) throws Throwable {
        // 트랜잭션 동기화가 활성화되어 있으면 커밋/롤백 후 해제하도록 등록
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            for (RLock lock : locks) {
                                if (lock.isHeldByCurrentThread()) {
                                    lock.unlock();
                                }
                            }
                        }
                    });
            // 실제 메서드 실행
            return pjp.proceed();
        }
        // 트랜잭션 없으면, 메서드 종료 후 바로 해제
        try {
            return pjp.proceed();
        } finally {
            for (RLock lock : locks) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }
```

### 주문 분산락 적용 (재고 차감)

주문에 분산락을 적용할 때 가장 고민이 되었던 부분은 복수의 상품을 주문하게 되는 경우에 여러 개의 lock을 획득해야 한다는 조건이 있었습니다.

이를 위해서 key를 생성할 때 사용되는 spEL을 위해 criteria 객채에 productId list를 생성해주는 메서드를 추가하였습니다.

```java
        public List<Long> toLockKeys() {
            return orderItems.stream()
                    .map(OrderItem::productId)
                    .sorted()
                    .collect(Collectors.toList());
        }
```

이후, Aspect에서는 해당 key가 list로 들어올 시 다수의 lock을 생성하는 로직을 추가하여 해당 기능을 구현하였습니다.

```java

private List<String> parseKeys(String topic, String spel, String[] paramNames, Object[] args) {

    Object key = parseKey(spel, paramNames, args);
    List<String> keys;

    if(key instanceof List) {
        keys = ((Collection<Object>) key).stream()
                .map(Object::toString)
                .map(id -> makeKeyName(topic, id))
                .sorted()
                .collect(Collectors.toList());
    }else if(key != null){
        keys = List.of(makeKeyName(topic, key.toString()));
    }else {
        throw new IllegalStateException("올바르지 않은 키 형식입니다.");
    }

    return keys;
}

private List<RLock> getLocks(List<String> keys, DistributedLock ann)throws InterruptedException {
    List<RLock> locks = new ArrayList<>(keys.size());

    for(String lockKey : keys){
        RLock lock = ann.type() == LockType.FAIR
                ? redisson.getFairLock(lockKey) : redisson.getLock(lockKey);

        boolean acquired = lock.tryLock(
                ann.waitTime(), ann.leaseTime(), ann.unit());

        if (!acquired) {
            locks.forEach(l -> {
                if (l.isHeldByCurrentThread()) l.unlock();
            });
            throw new IllegalStateException("락 획득 실패: " + lockKey);
        }

        locks.add(lock);
    }

    return locks;
}
```

단일 키로 주어도, 로직을 통일시키기 위해 list로 변환하였고, 이를 통해 한 method에서 다수의 lock을 생성하는 케이스도 커버하였습니다.