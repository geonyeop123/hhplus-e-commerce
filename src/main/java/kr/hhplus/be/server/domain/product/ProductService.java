package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.common.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public Product find(Long productId) {
        return productRepository.find(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 상품이 없습니다."));
    }

    public PageResult<Product> findAll(ProductCommand command) {

        long totalCount = productRepository.findProductCount();

        List<Product> products = productRepository.findAll(command);

        if(null == products){
            products = new ArrayList<>();
        }

        return PageResult.create(products, command.pageNo(), command.pageSize(), totalCount);
    }

    public Product validatePurchase(Long productId, int quantity){
        Product product = find(productId);
        product.validate(quantity);
        return product;
    }

    public Product deductStock(Long productId, int amount) {

        Product product = find(productId);
        product.deductStock(amount);

        productRepository.save(product);

        return product;
    }
}
