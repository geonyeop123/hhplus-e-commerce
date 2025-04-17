package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.common.PageResult;
import kr.hhplus.be.server.infra.product.JpaProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
@Transactional
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Nested
    class find {

        @DisplayName("상품 단건 조회를 할 수 있다.")
        @Test
        void singleFind() {
            // given
            Product product = Product.create("사과", 50, 1000);
            Product savedProduct = jpaProductRepository.save(product);

            // when
            Product findProduct = productService.find(savedProduct.getId());

            // then
            assertThat(findProduct.getId()).isEqualTo(savedProduct.getId());
            assertThat(findProduct.getName()).isEqualTo(savedProduct.getName());
            assertThat(findProduct.getPrice()).isEqualTo(savedProduct.getPrice());
            assertThat(findProduct.getStock()).isEqualTo(savedProduct.getStock());
        }

        @DisplayName("상품을 조회할 때 페이징 된 상품 목록을 생성일자 내림차순으로 조회할 수 있다.")
        @Test
        void findAll() {
            // given
            int page = 1;
            int size = 10;
            jpaProductRepository.saveAll(
                    List.of(
                            Product.create("사과", 30, 1000)
                            , Product.create("배", 40, 2000)
                            , Product.create("귤", 50, 3000)
                    )
            );

            // when
            ProductCommand.FindAll command = new ProductCommand.FindAll(page, size);
            PageResult<Product> pageResult = productService.findAll(command);

            // then
            assertThat(pageResult.page()).isEqualTo(page);
            assertThat(pageResult.size()).isEqualTo(size);
            assertThat(pageResult.totalCount()).isEqualTo(3);
            assertThat(pageResult.content())
                .extracting("name", "stock", "price")
                .containsExactly(
                    tuple("귤", 50, 3000),
                    tuple("배", 40, 2000),
                    tuple("사과", 30, 1000)
                );
        }
    }

    @DisplayName("구매 가능한 상품인 경우 Exception이 발생하지 않고 해당 상품이 조회된다.")
    @Test
    void validatePurchase() {
        // given
        Product product = Product.create("사과", 50, 1000);
        Product savedProduct = jpaProductRepository.save(product);
        Long productId = savedProduct.getId();
        int quantity = 3;

        // when
        ProductCommand.ValidatePurchase command = new ProductCommand.ValidatePurchase(productId, quantity);
        ProductInfo validatedPurchase = productService.validatePurchase(command);
        // then
        assertThat(validatedPurchase.id()).isEqualTo(productId);
    }

    @DisplayName("재고가 유효하여 차감할 수 있는 상품인 경우 차감 후 상품 정보를 반환한다.")
    @Test
    void deductStock() {
        // given
        int originalStock = 50;
        Product product = Product.create("사과", originalStock, 1000);
        Product savedProduct = jpaProductRepository.save(product);
        Long productId = savedProduct.getId();
        int quantity = 3;

        // when
        ProductCommand.DeductStock command = new ProductCommand.DeductStock(productId, quantity);
        ProductInfo deductedStockProduct = productService.deductStock(command);

        // then
        assertThat(deductedStockProduct.stock()).isEqualTo(originalStock - quantity);
    }
}