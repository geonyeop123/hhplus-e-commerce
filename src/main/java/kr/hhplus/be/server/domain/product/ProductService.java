package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.common.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product find(Long id) {
        return productRepository.find(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 상품이 없습니다."));
    }

    @Transactional(readOnly = true)
    public PageResult<Product> findAll(ProductCommand.FindAll command) {
        // Pageable을 활용한 페이징 처리 시 0부터 페이지를 count하기 때문에 1 감소
        int pageNo = command.pageNo() - 1;
        Pageable pageable = PageRequest.of(pageNo, command.pageSize(), Sort.by("createdAt").descending());
        Page<Product> page = productRepository.findAll(pageable);
        return PageResult.create(page.getContent(), command.pageNo(), command.pageSize(), page.getTotalElements());
    }



    public ProductInfo validatePurchase(ProductCommand.ValidatePurchase command){
        Product product = find(command.productId());
        product.validatePurchasable(command.quantity());
        return ProductInfo.from(product);
    }

    public ProductInfo deductStock(ProductCommand.DeductStock command) {

        Product product = find(command.productId());
        product.deductStock(command.quantity());

        return ProductInfo.from(product);
    }
}
