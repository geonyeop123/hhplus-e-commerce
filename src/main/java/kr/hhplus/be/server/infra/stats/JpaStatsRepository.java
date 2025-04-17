package kr.hhplus.be.server.infra.stats;

import kr.hhplus.be.server.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaStatsRepository extends JpaRepository<Order, Long> {

    @Query(value = """
        SELECT op.product_id AS productId, 
               SUM(op.quantity) AS totalQuantity, 
               p.name, 
               p.price, 
               p.stock
        FROM order_product op
        JOIN orders o ON op.order_id = o.id
        JOIN product p ON op.product_id = p.id
        WHERE o.status = 'SUCCESS'
          AND o.order_date_time >= NOW() - INTERVAL 3 DAY
        GROUP BY op.product_id, p.name, p.price, p.stock
        ORDER BY totalQuantity DESC
        LIMIT 5
    """, nativeQuery = true)
    List<NativePopularProduct> getPopularProducts();
}
