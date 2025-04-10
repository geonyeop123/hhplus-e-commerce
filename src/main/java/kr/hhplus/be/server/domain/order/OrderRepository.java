package kr.hhplus.be.server.domain.order;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {

    public Order save(Order order);

}
