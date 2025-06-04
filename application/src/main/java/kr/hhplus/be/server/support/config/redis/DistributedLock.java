package kr.hhplus.be.server.support.config.redis;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/*
*
* @Transaction
  @RedisLock(
      key = "'lock:point:' + #userId",
      waitTime = 5, leaseTime = 30, type = LockType.FAIR, unit = TimeUnit.SECONDS)
* charge(){
*  ...
* }
*
* */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    String topic();

    /**
     * 락의 키(SpEL 가능)
     * ex) "#userId"
     */
    String key();

    LockType type() default LockType.SIMPLE;

    /** 최대 대기 시간(tryLock) */
    long waitTime() default 5;

    /** 락 자동 만료 시간(leaseTime) */
    long leaseTime() default 30;

    /** 시간 단위 */
    TimeUnit unit() default TimeUnit.SECONDS;
}
