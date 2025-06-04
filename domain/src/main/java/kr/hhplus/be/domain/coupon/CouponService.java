package kr.hhplus.be.domain.coupon;

import kr.hhplus.be.support.exception.AlreadyIssuedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Coupon register(CouponCommand.Register command){

        Coupon coupon = Coupon.create(command.name(), command.type()
                , command.discountType(), command.discountAmount()
                , command.expirationMonth(), command.issueStartDate()
                , command.issueEndDate(), command.initialQuantity());

        return couponRepository.save(coupon);
    }

    public void issueCall(CouponCommand.IssueCall command) {
        Long userId = command.user().getId();
        Long couponId = command.couponId();
        boolean isFail = !couponRepository.issueCall(couponId, userId);
        if(isFail){
            throw new AlreadyIssuedException("이미 발급 요청한 쿠폰입니다.", userId, couponId);
        }

        applicationEventPublisher.publishEvent(new CouponEvent.IssueCalled(couponId, userId, LocalDateTime.now()));
    }

    @Transactional(readOnly = true)
    public List<Coupon> findIssueCouponList() {
        return null;
    }


    @Transactional
    public Coupon issueValidate(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        coupon.isValid(LocalDate.now());

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
