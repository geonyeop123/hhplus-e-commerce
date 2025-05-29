package kr.hhplus.be.server.application.ranking;

import kr.hhplus.be.domain.product.ProductInfo;
import kr.hhplus.be.domain.product.ProductService;
import kr.hhplus.be.domain.ranking.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingFacade {

    private final RankingService rankingService;
    private final ProductService productService;

    public void saveSalesProduct(RankingCriteria criteria) {

        RankingCommand.SaveSalesProduct rankingCommand = new RankingCommand.SaveSalesProduct(criteria.orderInfo().orderProducts(), criteria.orderInfo().orderDateTime());
        rankingService.saveSalesProduct(rankingCommand);
        // product cache 처리
        criteria.orderInfo().orderProducts().forEach(orderProduct -> {
            productService.find(orderProduct.getProductId());
        });
    }

    @Transactional(readOnly = true)
    public Ranking findDailyRankingProducts() {
        List<SalesProduct> dailySalesProducts = rankingService.findDailySalesProducts();
        dailySalesProducts.forEach(salesProduct -> {
            ProductInfo productInfo = productService.find(salesProduct.getProductId());
            salesProduct.setProductInfo(productInfo);
        });
        return new Ranking(RankingType.DAILY, dailySalesProducts);
    }
}
