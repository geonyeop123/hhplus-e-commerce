package kr.hhplus.be.domain.stats;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "stats_daily_product_sales",
        indexes = {
                @Index(name = "idx_product_id_order_date", columnList = "product_id, order_date"),
        }
)
public class DailySalesProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long productId;

    int salesCount;

    LocalDate orderDate;

    public DailySalesProduct(Long productId, int salesCount, LocalDate orderDate) {
        this.productId = productId;
        this.salesCount = salesCount;
        this.orderDate = orderDate;
    }

    public static DailySalesProduct create(Long productId, int salesCount, LocalDate orderDate){
        return new DailySalesProduct(productId, salesCount, orderDate);
    }
}
