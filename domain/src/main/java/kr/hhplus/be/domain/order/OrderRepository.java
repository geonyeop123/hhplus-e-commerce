package kr.hhplus.be.domain.order;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);
    Optional<Order> findById(Long id);
}
