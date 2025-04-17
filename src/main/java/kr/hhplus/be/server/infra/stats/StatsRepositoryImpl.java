package kr.hhplus.be.server.infra.stats;


import kr.hhplus.be.server.domain.stats.PopularProduct;
import kr.hhplus.be.server.domain.stats.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatsRepositoryImpl implements StatsRepository {

    private final JpaStatsRepository jpaStatsRepository;

    @Override
    public List<PopularProduct> getPopularProducts() {
        List<NativePopularProduct> NativePopularProducts = jpaStatsRepository.getPopularProducts();
        return NativePopularProducts.stream().map(NativePopularProduct -> {
            return new PopularProduct(NativePopularProduct.getProductId(), NativePopularProduct.getTotalQuantity(), NativePopularProduct.getName(), NativePopularProduct.getPrice(), NativePopularProduct.getStock());
        }).toList();
    }
}
