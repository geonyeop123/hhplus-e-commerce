package kr.hhplus.be.domain.userCoupon;

import kr.hhplus.be.domain.common.PageResult;
import kr.hhplus.be.domain.coupon.Coupon;
import kr.hhplus.be.domain.coupon.CouponType;
import kr.hhplus.be.domain.coupon.DiscountType;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCouponServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserCouponRepository userCouponRepository;

    @InjectMocks
    private UserCouponService userCouponService;

    @DisplayName("userId로 해당 유저가 보유한 쿠폰을 모두 조회할 수 있다.")
    @Test
    void findAllByUserId() {
        // given
        User user = User.create("yeop");
        Long userId = user.getId();
        UserCouponCommand.FindAll command = new UserCouponCommand.FindAll(user, 1, 10);

        List<UserCoupon> userCoupons = List.of(
                UserCoupon.builder().userId(1L).couponId(1L).build()
                , UserCoupon.builder().userId(1L).couponId(1L).build()
        );

        Pageable pageable = PageRequest.of(command.pageNo() - 1, command.pageSize(), Sort.by("createdAt").descending());
        Page<UserCoupon> pageResult = new PageImpl<>(userCoupons, pageable, userCoupons.size());
        when(userCouponRepository.findAllByUserId(userId, pageable)).thenReturn(pageResult);

        // when
        PageResult<UserCoupon> result = userCouponService.findAllByUserId(command);

        // then
        assertThat(result.content()).hasSize(2);
        assertThat(result.page()).isEqualTo(1);
        assertThat(result.size()).isEqualTo(10);
        assertThat(result.totalCount()).isEqualTo(2);
        assertThat(result.totalPages()).isEqualTo(1);

        verify(userCouponRepository, times(1)).findAllByUserId(userId, pageable);
    }

    @Nested
    class issue{

        @DisplayName("user와 Coupon으로 user에게 userCoupon을 발급할 수 있다.")
        @Test
        void success() {
            // given
            User user = User.create("yeop");
            Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), 50);
            UserCoupon userCoupon = UserCoupon.builder().userId(1L).couponId(1L).build();
            UserCouponCommand.Issue command = new UserCouponCommand.Issue(user, coupon);
            when(userCouponRepository.save(any(UserCoupon.class))).thenReturn(userCoupon);
            // when

            userCouponService.issue(command);

            // then
            verify(userCouponRepository, times(1)).save(any(UserCoupon.class));
        }

        @DisplayName("해당 쿠폰을 이미 발급받은 유저인 경우 발급이 되지 않고 이전의 발급된 쿠폰 정보가 반환된다.")
        @Test
        void fail() {
            // given
            User user = User.create("yeop");
            Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), 50);
            UserCoupon userCoupon = UserCoupon.builder().userId(1L).couponId(1L).build();
            UserCouponCommand.Issue command = new UserCouponCommand.Issue(user, coupon);
            when(userCouponRepository.findByUserIdAndCouponId(user.getId(), coupon.getId())).thenReturn(Optional.of(userCoupon));

            // when
            UserCoupon issuedCoupon = userCouponService.issue(command);

            // then
            verify(userCouponRepository, times(1)).findByUserIdAndCouponId(user.getId(), coupon.getId());
            assertThat(issuedCoupon.getId()).isEqualTo(userCoupon.getId());
        }

    }

    @Nested
    class find {
        @DisplayName("userCouponId로 해당하는 쿠폰을 조회할 수 있다.")
        @Test
        void findByIdSuccess() {
            // given
            Long userCouponId = 1L;
            UserCoupon userCoupon = UserCoupon.builder().couponId(1L).name("깜짝 쿠폰").discountAmount(5000).build();
            when(userCouponRepository.findById(userCouponId)).thenReturn(Optional.of(userCoupon));

            // when
            UserCoupon findUserCoupon = userCouponService.findById(userCouponId);

            // then
            assertThat(findUserCoupon).isNotNull();
            verify(userCouponRepository, times(1)).findById(userCouponId);
        }

        @DisplayName("userCouponId에 해당하는 쿠폰이 없는 경우에 IllegalArgumentException이 발생한다.")
        @Test
        void findByIdFail() {
            // given
            Long userCouponId = 1L;

            // when // then
            assertThatThrownBy(() -> userCouponService.findById(userCouponId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("조회된 쿠폰이 없습니다.");
        }
    }

    @DisplayName("유효성 검사를 진행한 유저쿠폰 정보를 조회할 수 있다.")
    @Test
    void validateAndGetInfo() {
        // given
        Long userId = 1L;
        Long userCouponId = 1L;
        UserCoupon userCoupon =
                UserCoupon.builder().userId(userId).couponId(1L)
                        .name("깜짝 쿠폰").expiredAt(LocalDate.now()
                                .plusMonths(3)).discountAmount(5000).build();
        when(userCouponRepository.findById(userCouponId)).thenReturn(Optional.of(userCoupon));
        UserCouponCommand.Validate command = new UserCouponCommand.Validate(userId, userCouponId);

        // when
        UserCouponInfo validatedUserCoupon = userCouponService.validateAndGetInfo(command);

        // then
        assertThat(validatedUserCoupon).isNotNull();
        verify(userCouponRepository, times(1)).findById(userCouponId);
    }

    @DisplayName("쿠폰 사용처리를 할 수 있다.")
    @Test
    void use() {
        // given
        Long userId = 1L;
        Long userCouponId = 1L;
        UserCoupon userCoupon =
                UserCoupon.builder().userId(userId).couponId(1L)
                        .name("깜짝 쿠폰").expiredAt(LocalDate.now()
                        .plusMonths(3)).discountAmount(5000).build();
        when(userCouponRepository.findById(userCouponId)).thenReturn(Optional.of(userCoupon));
        UserCouponCommand.Use command = new UserCouponCommand.Use(userId, userCouponId);

        // when
        UserCouponInfo usedUserCoupon = userCouponService.use(command);

        // then
        assertThat(usedUserCoupon).isNotNull();
        assertThat(usedUserCoupon.usedAt()).isNotNull();
        verify(userCouponRepository, times(1)).findById(userCouponId);
    }

}