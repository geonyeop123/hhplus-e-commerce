package kr.hhplus.be.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.domain.common.BaseEntity;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.userCoupon.UserCoupon;
import kr.hhplus.be.support.exception.CouponIssueLimitExceededException;
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

    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;

    private int discountAmount;
    private int expirationMonth;
    private LocalDate issueStartDate;
    private LocalDate issueEndDate;
    private int initialQuantity;
    private int quantity;

    public UserCoupon issueTo(User user) {
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

    public boolean isValid(LocalDate today) {
        if(today.isBefore(issueStartDate) || today.isAfter(issueEndDate)){
            return false;
        }else return quantity >= 1;
    }

    public void finishIssue() {
        this.issueStatus = IssueStatus.FINISH;
    }

    public void deductQuantity() {
        if(quantity < 1) {
            throw new CouponIssueLimitExceededException(this.id);
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
        this.issueStatus = IssueStatus.ING;
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
