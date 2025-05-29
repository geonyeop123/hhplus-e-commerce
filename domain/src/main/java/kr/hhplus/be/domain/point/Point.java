package kr.hhplus.be.domain.point;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.domain.common.BaseEntity;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.support.exception.InsufficientBalanceException;
import kr.hhplus.be.support.exception.MaximumBalanceException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int balance;

    public static Point create(User user, int balance) {
        return new Point(user, balance);
    }

    @Builder
    public Point(Long id, User user, int balance) {
        this.id = id;
        this.userId = user.getId();
        this.balance = balance;
    }

    private Point(User user, int balance) {
        this.userId = user.getId();
        this.balance = balance;
    }

    public void charge(int amount) {
        int balance = this.balance + amount;

        if(PointRule.CHARGE.getMin() >= amount){
            throw new IllegalArgumentException("포인트는 1포인트 이상부터 충전이 가능합니다.");
        }else if(PointRule.CHARGE.getMax() < balance){
            throw new MaximumBalanceException(balance);
        }

        this.balance = balance;
    }

    public void use(int amount) {

        int usedBalance = this.balance - amount;

        if(amount <= 0){
            throw new IllegalArgumentException("포인트는 1포인트 이상부터 사용 가능합니다.");
        }else if(usedBalance < 0){
            throw new InsufficientBalanceException(this.balance, amount);
        }
        balance = usedBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;
        return Objects.equals(id, point.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
