package kr.hhplus.be.server.domain.coupon;

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
}
