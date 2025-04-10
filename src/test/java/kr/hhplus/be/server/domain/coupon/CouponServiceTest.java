package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.userCoupon.UserCouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserCouponRepository userCouponRepository;

    @InjectMocks
    private CouponService couponService;

    @DisplayName("쿠폰 발급을 할 수 있다.")
    @Test
    void issue() {
        // given
        Long userId = 1L;
        Long couponId = 1L;
        CouponCommand command = new CouponCommand(userId, couponId);
        User user = User.create(userId, "yeop");
        Coupon coupon = Coupon.create(couponId, "4월 깜짝 쿠폰", 5000, 5, 50);
        UserCoupon userCoupon = UserCoupon.builder().couponId(couponId).userId(userId).build();

        when(userService.findByUserId(userId)).thenReturn(user);
        when(couponRepository.findById(couponId)).thenReturn(coupon);
        when(userCouponRepository.save(userCoupon)).thenReturn(userCoupon);

        // when
        UserCoupon issuedCoupon = couponService.issue(command);

        // then
        assertThat(issuedCoupon).isEqualTo(userCoupon);
        verify(userService, times(1)).findByUserId(userId);
        verify(couponRepository, times(1)).findById(couponId);
        verify(userCouponRepository, times(1)).save(userCoupon);

    }

}