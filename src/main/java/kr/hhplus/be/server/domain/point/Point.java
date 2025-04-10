package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class Point {

    private User user;
    private int balance;

    public static Point create(User user, int balance) {
        return new Point(user, balance);
    }

    private Point(User user, int balance) {
        this.user = user;
        this.balance = balance;
    }

    public void charge(int amount) {
        int balance = this.balance + amount;

        if(PointRule.CHARGE.getMin() >= amount){
            throw new IllegalArgumentException("포인트는 1포인트 이상부터 충전이 가능합니다.");
        }else if(PointRule.CHARGE.getMax() < balance){
            throw new IllegalArgumentException("충전 이후 포인트는 10,000,000포인트를 넘을 수 없습니다.");
        }

        this.balance = balance;
    }

    public void use(int amount) {

        int usedBalance = this.balance - amount;

        if(amount <= 0){
            throw new IllegalArgumentException("포인트는 1포인트 이상부터 사용 가능합니다.");
        }else if(usedBalance <= 0){
            throw new IllegalArgumentException("보유 포인트가 부족합니다.");
        }
        balance = usedBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;
        return balance == point.balance && Objects.equals(user, point.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, balance);
    }
}
