package kr.hhplus.be.domain.userCoupon;


import jakarta.persistence.*;
import kr.hhplus.be.domain.common.BaseEntity;
import kr.hhplus.be.domain.coupon.CouponType;
import kr.hhplus.be.domain.coupon.DiscountType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "user_coupon",
        indexes = {
                // 복합 인덱스 (user_id + coupon_id 순으로 정렬)
                @Index(name = "idx_user_coupon_user_coupon", columnList = "user_id, coupon_id")
        }
)
public class UserCoupon extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long couponId;
    private String name;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private int discountAmount;

    private LocalDateTime usedAt;

    private LocalDate expiredAt;

    @Builder
    private UserCoupon(Long userId, Long couponId, String name, CouponType type,
                       DiscountType discountType, int discountAmount, LocalDateTime usedAt, LocalDate expiredAt) {
        this.userId = userId;
        this.couponId = couponId;
        this.name = name;
        this.type = type;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.usedAt = usedAt;
        this.expiredAt = expiredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        UserCoupon that = (UserCoupon) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void validate(Long userId) {
        if(!this.userId.equals(userId)){
            throw new IllegalArgumentException("보유 중인 쿠폰이 아닙니다.");
        }else if(isExpiration()){
            throw new ExpiredException("유효기간이 만료된 쿠폰입니다.");
        }else if(isUsed()){
            throw new AlreadyUsedException("이미 사용된 쿠폰입니다.");
        }
    }

    public boolean isExpiration() {
        return LocalDate.now().isAfter(this.expiredAt);
    }

    public void use(Long userId) {
        validate(userId);
        this.usedAt = LocalDateTime.now();
    }

    public boolean isUsed(){
        return usedAt != null;
    }

}
