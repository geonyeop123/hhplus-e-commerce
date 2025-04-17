package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.support.exception.CouponIssueLimitExceededException;
import kr.hhplus.be.server.support.exception.CouponIssuePeriodException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private int discountAmount;
    private int expirationMonth;
    private LocalDate issueStartDate;
    private LocalDate issueEndDate;
    private int initialQuantity;
    private int quantity;

    public UserCoupon issueTo(User user, LocalDate today) {
        deductQuantity(today);

        return UserCoupon.builder()
                .couponId(this.id)
                .userId(user.getId())
                .name(this.name)
                .type(this.type)
                .discountType(this.discountType)
                .discountAmount(this.discountAmount)
                .expiredAt(LocalDate.now().plusMonths(this.expirationMonth))
                .build();
    }

    private void deductQuantity(LocalDate today) {
        if(today.isBefore(issueStartDate) || today.isAfter(issueEndDate)){
            throw new CouponIssuePeriodException();
        }else if(quantity < 1) {
            throw new CouponIssueLimitExceededException();
        }

        quantity--;
    }

    public static Coupon create(String name, CouponType type, DiscountType discountType, int discountAmount, int expirationMonth, LocalDate issueStartDate, LocalDate issueEndDate, int initialQuantity) {
        return new Coupon(name, type, discountType, discountAmount, expirationMonth, issueStartDate, issueEndDate, initialQuantity, initialQuantity);
    }

    private Coupon(String name, CouponType type, DiscountType discountType, int discountAmount, int expirationMonth, LocalDate issueStartDate, LocalDate issueEndDate, int initialQuantity, int quantity) {
        this.name = name;
        this.type = type;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.expirationMonth = expirationMonth;
        this.issueStartDate = issueStartDate;
        this.issueEndDate = issueEndDate;
        this.initialQuantity = initialQuantity;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
