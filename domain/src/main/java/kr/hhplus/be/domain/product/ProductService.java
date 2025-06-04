package kr.hhplus.be.domain.product;
import kr.hhplus.be.domain.common.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "product", key = "#id")
    public ProductInfo find(Long id) {
        Product product = productRepository.find(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 상품이 없습니다."));
        return ProductInfo.from(product);
    }

    @Transactional(readOnly = true)
    public PageResult<ProductInfo> findAll(ProductCommand.FindAll command) {
        // Pageable을 활용한 페이징 처리 시 0부터 페이지를 count하기 때문에 1 감소
        int pageNo = command.pageNo() - 1;
        Pageable pageable = PageRequest.of(pageNo, command.pageSize(), Sort.by("createdAt").descending());
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductInfo> ProductList = page.getContent().stream().map(ProductInfo::from).toList();
        return PageResult.create(ProductList, command.pageNo(), command.pageSize(), page.getTotalElements());
    }



    public ProductInfo validatePurchase(ProductCommand.ValidatePurchase command){
        Product product = productRepository.find(command.productId())
                .orElseThrow(() -> new IllegalArgumentException("존재하는 상품이 없습니다."));
        product.validatePurchasable(command.quantity());
        return ProductInfo.from(product);
    }

    @Transactional
    public ProductInfo deductStock(ProductCommand.DeductStock command) {

        Product product = productRepository.findByIdForUpdate(command.productId())
                .orElseThrow(() -> new IllegalArgumentException("존재하는 상품이 없습니다."));

        product.deductStock(command.quantity());

        return ProductInfo.from(product);
    }
}
