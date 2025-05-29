package kr.hhplus.be.domain.userCoupon;

import kr.hhplus.be.domain.common.PageResult;
import kr.hhplus.be.domain.coupon.Coupon;
import kr.hhplus.be.domain.coupon.CouponType;
import kr.hhplus.be.domain.coupon.DiscountType;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.infra.coupon.JpaCouponRepository;
import kr.hhplus.be.infra.user.JpaUserRepository;
import kr.hhplus.be.infra.userCoupon.JpaUserCouponRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create"
})
@Transactional
class UserCouponServiceIntegrationTest {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private JpaUserCouponRepository jpaUserCouponRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaCouponRepository jpaCouponRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    @Nested
    class findIssueTarget{
        @DisplayName("쿠폰 발급 대상을 조회할 때 등록 순서대로 조회한다.")
        @Test
        void findIssueTargetOrderByCall() {
            // given
            Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), 10);
            jpaCouponRepository.save(coupon);
            UserCouponCommand.FindIssueTarget command = new UserCouponCommand.FindIssueTarget(coupon.getId(), 5);

            final String KEY_PREFIX = "coupon:";
            final String CALL_KEY_SUFFIX = ":callIssue";
            String callKey = KEY_PREFIX + coupon.getId() + CALL_KEY_SUFFIX;
            long SystemTime = System.currentTimeMillis();
            redisTemplate.opsForZSet().add(callKey, "userId:1", SystemTime);
            redisTemplate.opsForZSet().add(callKey, "userId:2", SystemTime + 1);
            redisTemplate.opsForZSet().add(callKey, "userId:3", SystemTime + 2);
            redisTemplate.opsForZSet().add(callKey, "userId:4", SystemTime + 3);
            redisTemplate.opsForZSet().add(callKey, "userId:5", SystemTime + 4);
            // when
            List<Long> issueTargetUserIds = userCouponService.findIssueTargetUserIds(command);

            // then
            assertThat(issueTargetUserIds).hasSize(5);
            assertThat(issueTargetUserIds).containsExactly(1L, 2L, 3L, 4L, 5L);
        }

        @DisplayName("쿠폰 발급 대상을 조회할 때 발급 수보다 많은 대기인원이 있어도 발급 수만큼만 조회한다.")
        @Test
        void findIssueTargetOverSetSize() {
            // given
            Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), 10);
            jpaCouponRepository.save(coupon);
            UserCouponCommand.FindIssueTarget command = new UserCouponCommand.FindIssueTarget(coupon.getId(), 5);

            final String KEY_PREFIX = "coupon:";
            final String CALL_KEY_SUFFIX = ":callIssue";
            String callKey = KEY_PREFIX + coupon.getId() + CALL_KEY_SUFFIX;
            long SystemTime = System.currentTimeMillis();
            redisTemplate.opsForZSet().add(callKey, "userId:1", SystemTime);
            redisTemplate.opsForZSet().add(callKey, "userId:2", SystemTime + 1);
            redisTemplate.opsForZSet().add(callKey, "userId:3", SystemTime + 2);
            redisTemplate.opsForZSet().add(callKey, "userId:4", SystemTime + 3);
            redisTemplate.opsForZSet().add(callKey, "userId:5", SystemTime + 4);
            redisTemplate.opsForZSet().add(callKey, "userId:6", SystemTime + 5);
            redisTemplate.opsForZSet().add(callKey, "userId:7", SystemTime + 6);
            // when
            List<Long> issueTargetUserIds = userCouponService.findIssueTargetUserIds(command);

            // then
            assertThat(issueTargetUserIds).hasSize(5);
            assertThat(issueTargetUserIds).containsExactly(1L, 2L, 3L, 4L, 5L);
        }

        @DisplayName("쿠폰 발급 대상을 조회할 때 요청한 대상이 없으면 빈 객체가 반환된다.")
        @Test
        void findIssueTargetEmptyList() {
            // given
            Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), 10);
            jpaCouponRepository.save(coupon);
            UserCouponCommand.FindIssueTarget command = new UserCouponCommand.FindIssueTarget(coupon.getId(), 5);

            // when
            List<Long> issueTargetUserIds = userCouponService.findIssueTargetUserIds(command);

            // then
            assertThat(issueTargetUserIds).isEmpty();
        }
    }

    // 단건 조회
    @DisplayName("id로 해당하는 userCoupon을 조회할 수 있다.")
    @Test
    void findById() {
        // given
        UserCoupon userCoupon = createUserCoupon(1L, 1L, "깜짝쿠폰1", 5000);

        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);
        Long id = savedUserCoupon.getId();

        // when
        UserCoupon findUserCoupon = userCouponService.findById(id);

        // then
        assertThat(findUserCoupon).isEqualTo(savedUserCoupon);
    }

    // 목록 조회
    @DisplayName("userid에 해당하는 유저가 보유한 userCoupon을 페이징 처리된 목록으로 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        User user = User.create("yeop");
        User savedUser = jpaUserRepository.save(user);
        Long userId = savedUser.getId();

        jpaUserCouponRepository.saveAll(List.of(
                createUserCoupon(userId, 1L, "깜짝쿠폰1", 3000)
                , createUserCoupon(userId, 1L, "깜짝쿠폰2", 4000)
                , createUserCoupon(userId, 1L, "깜짝쿠폰3", 5000)));

        // when
        UserCouponCommand.FindAll command = new UserCouponCommand.FindAll(user, 1, 10);
        PageResult<UserCoupon> pageResult = userCouponService.findAllByUserId(command);

        // then
        assertThat(pageResult.page()).isEqualTo(1);
        assertThat(pageResult.size()).isEqualTo(10);
        assertThat(pageResult.totalPages()).isEqualTo(1);
        assertThat(pageResult.totalCount()).isEqualTo(3);
        assertThat(pageResult.content()).extracting("userId", "couponId", "name", "discountAmount")
                .containsExactly(
                        tuple(userId, 1L, "깜짝쿠폰3", 5000),
                        tuple(userId, 1L, "깜짝쿠폰2", 4000),
                        tuple(userId, 1L, "깜짝쿠폰1", 3000));
    }

    @DisplayName("userId와 couponId로 couponId에 해당하는 쿠폰을 user에게 발급할 수 있다.")
    @Test
    void issue() {
        // given
        User user = User.create("yeop");
        User savedUser = jpaUserRepository.save(user);

        Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), 50);
        Coupon savedCoupon = jpaCouponRepository.save(coupon);
        final String KEY_PREFIX = "coupon:";
        final String CALL_KEY_SUFFIX = ":callIssue";
        final String ISSUED_KEY_SUFFIX = ":issued";
        String callKey = KEY_PREFIX + coupon.getId() + CALL_KEY_SUFFIX;
        long SystemTime = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(callKey, "userId:" + savedUser.getId(), SystemTime);

        // when
        UserCouponCommand.Issue command = new UserCouponCommand.Issue(savedUser, coupon);
        UserCoupon issuedCoupon = userCouponService.issue(command);

        // then
        UserCoupon savedUserCoupon = userCouponRepository
                .findById(issuedCoupon.getId()).orElse(null);
        assertThat(savedUserCoupon).isNotNull();
        assertThat(savedUserCoupon.getUserId()).isEqualTo(savedUser.getId());
        assertThat(savedUserCoupon.getCouponId()).isEqualTo(savedCoupon.getId());
        assertThat(savedUserCoupon.getName()).isEqualTo(savedCoupon.getName());
        assertThat(savedUserCoupon.getDiscountAmount()).isEqualTo(savedCoupon.getDiscountAmount());

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(callKey, 0, -1);
        assertThat(typedTuples).isEmpty();
        String issuedKey = KEY_PREFIX + coupon.getId() + ISSUED_KEY_SUFFIX;
        Boolean isMember = redisTemplate.opsForSet().isMember(issuedKey, "userId:" + user.getId());
        assertThat(isMember).isTrue();
    }

    // 유효성 검사
    @DisplayName("userId와 userCouponId를 통해 validation을 수행한 userCoupon을 조회할 수 있다.")
    @Test
    void validate() {
        // given
        User user = User.create("yeop");
        User savedUser = jpaUserRepository.save(user);
        UserCoupon userCoupon = createUserCoupon(savedUser.getId(), 1L, "깜짝쿠폰", 5000);
        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);
        Long userId = savedUser.getId();
        Long userCouponId = savedUserCoupon.getId();
        UserCouponCommand.Validate command = new UserCouponCommand.Validate(userId, userCouponId);
        // when

        UserCouponInfo validate = userCouponService.validateAndGetInfo(command);

        // then
        assertThat(validate).isNotNull();
    }

    @DisplayName("userId와 userCouponId와 orderId를 통해 userCoupon을 사용처리 할 수 있다.")
    @Test
    void use() {
        // given
        User user = User.create("yeop");
        User savedUser = jpaUserRepository.save(user);
        UserCoupon userCoupon = createUserCoupon(savedUser.getId(), 1L, "깜짝쿠폰", 5000);
        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);
        Long userId = savedUser.getId();
        Long userCouponId = savedUserCoupon.getId();
        UserCouponCommand.Use command = new UserCouponCommand.Use(userId, userCouponId);

        // when
        UserCouponInfo usedUserCoupon = userCouponService.use(command);

        // then
        assertThat(usedUserCoupon.usedAt()).isNotNull();
    }

    private UserCoupon createUserCoupon(Long userId, Long couponId, String name, int discountAmount) {
        return UserCoupon.builder().userId(userId).couponId(couponId).name(name).discountAmount(discountAmount).expiredAt(LocalDate.now().plusDays(3)).build();
    }

}