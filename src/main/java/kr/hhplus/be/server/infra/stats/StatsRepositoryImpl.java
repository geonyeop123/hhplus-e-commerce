package kr.hhplus.be.server.infra.stats;


import kr.hhplus.be.server.domain.stats.PopularProduct;
import kr.hhplus.be.server.domain.stats.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatsRepositoryImpl implements StatsRepository {

    private final NamedParameterJdbcTemplate jdbc;
    private final JpaStatsRepository jpaStatsRepository;

    public void batchInsert(List<SalesProductSummary> products) {
        String sql = """
      INSERT INTO stats_daily_product_sales
        (product_id, sales_count, order_date)
      VALUES (:productId, :salesCount, :orderDate)
      """;
        SqlParameterSource[] batch = products.stream()
                .map(p -> new MapSqlParameterSource()
                        .addValue("productId", p.getProductId())
                        .addValue("salesCount", p.getSalesCount())
                        .addValue("orderDate", p.getOrderDate())
                ).toArray(SqlParameterSource[]::new);
        jdbc.batchUpdate(sql, batch);
    }

    public List<SalesProductSummary> findSalesProductSummaryByDateTime(LocalDateTime datetime){
        return jpaStatsRepository.findSalesProductByDateTime(datetime);
    }

    @Override
    public List<PopularProduct> getPopularProducts(LocalDate startDate, LocalDate endDate) {
        List<NativePopularProduct> NativePopularProducts = jpaStatsRepository.getPopularProducts(startDate, endDate);
        return NativePopularProducts.stream().map(NativePopularProduct -> {
            return new PopularProduct(NativePopularProduct.getProductId(), NativePopularProduct.getTotalQuantity(), NativePopularProduct.getName(), NativePopularProduct.getPrice(), NativePopularProduct.getStock());
        }).toList();
    }
}
