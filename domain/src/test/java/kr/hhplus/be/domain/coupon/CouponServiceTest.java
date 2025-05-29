package kr.hhplus.be.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;


    @DisplayName("해당 쿠폰이 발급할 수 있는 상태인지 유효성 검사를 진행한 후 쿠폰을 반환한다.")
    @Test
    void issueValidate() {
        // given
        Long couponId = 1L;
        Coupon coupon = Coupon.create("깜짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 1000, 3, LocalDate.now().minusDays(1), LocalDate.now().plusDays(3), 10);
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));

        // when
        Coupon validatedCoupon = couponService.issueValidate(couponId);

        // then
        assertThat(validatedCoupon).isNotNull();
        verify(couponRepository, times(1)).findById(couponId);
    }

    @DisplayName("쿠폰의 수량을 차감시킬 수 있다.")
    @Test
    void deduct() {
        // given
        Long couponId = 1L;
        Coupon coupon = Coupon.create("깜짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 1000, 3, LocalDate.now().minusDays(1), LocalDate.now().plusDays(3), 10);
        when(couponRepository.findByIdForUpdate(couponId)).thenReturn(Optional.of(coupon));

        // when
        couponService.deduct(couponId);

        // then
        verify(couponRepository, times(1)).findByIdForUpdate(couponId);
    }

    @DisplayName("쿠폰을 생성하면 repository를 호출하여 저장한다.")
    @Test
    void register() {
        // given
        CouponCommand.Register command = new CouponCommand.Register("5000원 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now().minusDays(1), LocalDate.now().plusDays(3), 10);
        Coupon coupon = Coupon.create(command.name(), command.type(), command.discountType(), command.discountAmount(), command.expirationMonth(), command.issueStartDate(), command.issueEndDate(), command.initialQuantity());
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        // when
        Coupon register = couponService.register(command);

        // then
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

}