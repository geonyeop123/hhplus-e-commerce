package kr.hhplus.be.server.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @DisplayName("couponId로 해당 coupon을 조회할 수 있다.")
    @Test
    void find() {
        // given
        Long id = 1L;
        Coupon coupon = Coupon.create("깜짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), 10);
        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));

        // when
        couponService.findById(id);

        // then
        verify(couponRepository, times(1)).findById(id);
    }

}