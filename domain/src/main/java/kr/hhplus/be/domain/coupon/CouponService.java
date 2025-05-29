package kr.hhplus.be.domain.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public Coupon register(CouponCommand.Register command){

        Coupon coupon = Coupon.create(command.name(), command.type()
                , command.discountType(), command.discountAmount()
                , command.expirationMonth(), command.issueStartDate()
                , command.issueEndDate(), command.initialQuantity());

        return couponRepository.save(coupon);
    }

    @Transactional(readOnly = true)
    public List<Coupon> findIssueCouponList() {
        return null;
    }


    @Transactional
    public Coupon issueValidate(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        boolean notValid = !coupon.isValid(LocalDate.now());

        if(notValid){
            coupon.finishIssue();
        }

        return coupon;
    }

    @Transactional
    public Coupon deduct(Long id) {
        Coupon coupon = couponRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        coupon.deductQuantity();

        return coupon;
    }

}
