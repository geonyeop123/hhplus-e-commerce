package kr.hhplus.be.server.domain.userCoupon;

import kr.hhplus.be.server.support.exception.ExpiredException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class UserCoupon {
    private Long id;
    private Long userId;
    private Long couponId;
    private Long orderId;
    private String name;
    private int discountAmount;
    private LocalDateTime usedAt;
    private LocalDate expiredAt;

    @Builder
    private UserCoupon(Long id, Long userId, Long couponId, String name, int discountAmount, LocalDateTime usedAt, LocalDate expiredAt) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.name = name;
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
        }
    }

    public boolean isExpiration() {
        return LocalDate.now().isAfter(this.expiredAt);
    }

    public void use(Long userId, Long orderId) {
        validate(userId);
        this.orderId = orderId;
        this.usedAt = LocalDateTime.now();
    }
}
