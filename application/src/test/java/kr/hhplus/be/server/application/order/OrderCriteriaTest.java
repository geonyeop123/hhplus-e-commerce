package kr.hhplus.be.server.application.order;

import kr.hhplus.be.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderCriteriaTest {


    @DisplayName("orderItem으로 들어온 productId를 오름차순으로 정렬된 list로 받을 수 있다.")
    @Test
    void toLockKeys() {
        // given
        OrderCriteria.Create criteria = new OrderCriteria.Create(User.create("1"), 1L
                , List.of(
                        new OrderCriteria.Create.OrderItem(3L, 2),
                new OrderCriteria.Create.OrderItem(5L, 2),
                new OrderCriteria.Create.OrderItem(7L, 2)));

        // when
        List<Long> lockKeys = criteria.toLockKeys();

        // then
        assertThat(lockKeys).containsExactly(3L, 5L, 7L);
    }
}