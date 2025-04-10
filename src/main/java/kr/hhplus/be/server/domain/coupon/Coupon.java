package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public class Coupon {
    private Long id;
    private String name;
    private int discountAmount;
    private int expirationMonth;
    private int initialQuantity;
    private int quantity;

    public UserCoupon issueTo(User user) {
        deductQuantity();

        return UserCoupon.builder()
                .couponId(this.id)
                .userId(user.getId())
                .name(this.name)
                .discountAmount(this.discountAmount)
                .expiredAt(LocalDate.now().plusMonths(this.expirationMonth))
                .build();
    }

    private void deductQuantity() {
        if(quantity <= 0){
            throw new IllegalArgumentException("발급 가능한 수량을 초과하였습니다.");
        }
        quantity--;
    }

    public static Coupon create(Long id, String name, int discountAmount, int expirationMonth, int initialQuantity) {
        return new Coupon(id, name, discountAmount, expirationMonth, initialQuantity, initialQuantity);
    }

    private Coupon(Long id, String name, int discountAmount, int expirationMonth, int initialQuantity, int quantity) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.expirationMonth = expirationMonth;
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
