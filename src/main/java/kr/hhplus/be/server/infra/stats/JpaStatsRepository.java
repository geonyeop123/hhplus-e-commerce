package kr.hhplus.be.server.infra.stats;

import kr.hhplus.be.server.domain.stats.SalesProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface JpaStatsRepository extends JpaRepository<SalesProduct, Long> {

    @Query(value = """
        SELECT 
            s.product_id        AS productId,
            SUM(s.sales_count)  AS totalQuantity,
            p.name              AS name,
            p.price             AS price,
            p.stock             AS stock
        FROM stats_daily_product_sales s
        JOIN product p 
          ON p.id = s.product_id
        WHERE s.order_date BETWEEN :startDate AND :endDate
        GROUP BY 
            s.product_id,
            p.name,
            p.price,
            p.stock
        ORDER BY totalQuantity DESC
        LIMIT 5
    """, nativeQuery = true)
    List<NativePopularProduct> getPopularProducts(LocalDate startDate, LocalDate endDate);

    @Query(value = """
    SELECT
      op.product_id            AS productId,
      SUM(op.quantity)         AS salesCount,
      DATE(o.order_date_time)  AS orderDate
    FROM orders o
    JOIN order_product op ON op.order_id = o.id
    WHERE o.order_date_time >= :dateTime
      AND o.status = 'SUCCESS'
    GROUP BY op.product_id, orderDate
    ORDER BY salesCount DESC
    """, nativeQuery = true)
    List<SalesProductSummary> findSalesProductByDateTime(@Param("dateTime")LocalDateTime dateTime);

    @Query("""
      SELECT sp
        FROM SalesProduct sp
        JOIN FETCH sp.product p
       WHERE sp.orderDate = :orderDate
    """)
    List<SalesProduct> findByOrderDateWithProduct(@Param("orderDate") LocalDate orderDate);
}
