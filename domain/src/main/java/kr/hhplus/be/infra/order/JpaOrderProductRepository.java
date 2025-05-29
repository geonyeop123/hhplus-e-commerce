package kr.hhplus.be.infra.order;

import kr.hhplus.be.domain.order.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
