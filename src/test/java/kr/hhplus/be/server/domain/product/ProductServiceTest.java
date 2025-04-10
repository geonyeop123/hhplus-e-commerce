package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.common.PageResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Nested
    class find {
        @DisplayName("productId로 해당하는 상품을 조회할 수 있다.")
        @Test
        void success() {
            // given
            Long productId = 1L;
            Product req = makeProduct(productId, "사과", 5000, 50);
            when(productRepository.find(productId)).thenReturn(Optional.of(req));

            // when
            Product product = productService.find(productId);

            // then
            assertThat(product.getId()).isEqualTo(productId);
            verify(productRepository, times(1)).find(productId);
        }

        @DisplayName("productId에 해당하는 상품이 없을 시 IllegalArgumentException이 발생한다.")
        @Test
        void fail() {
            // given
            Long productId = 1L;

            // when // then
            assertThatThrownBy(() -> productService.find(productId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하는 상품이 없습니다.");

            verify(productRepository, times(1)).find(productId);
        }
    }


    @Nested
    class findAll {

        @DisplayName("pageNo와 pageSize를 받아 페이징된 상품 목록을 조회할 수 있다.")
        @Test
        void success() {
            // given
            ProductCommand command = new ProductCommand(1, 10);
            List<Product> content = List.of(
                    makeProduct(1L, "사과", 5000, 50),
                    makeProduct(2L, "배", 4000, 50)
            );
            when(productRepository.findProductCount()).thenReturn(2L);
            when(productRepository.findAll(command)).thenReturn(content);

            // when
            PageResult<Product> pageResult = productService.findAll(command);

            // then
            assertThat(pageResult.content()).hasSize(2);
            assertThat(pageResult.page()).isEqualTo(1);
            assertThat(pageResult.size()).isEqualTo(10);
            assertThat(pageResult.totalCount()).isEqualTo(2);
            assertThat(pageResult.totalPages()).isEqualTo(1);
            verify(productRepository, times(1)).findProductCount();
            verify(productRepository, times(1)).findAll(command);
        }
    }

    @Nested
    class deductStock {
        @DisplayName("차감할 재고만큼 재고가 충분한 경우 재고를 차감할 수 있다.")
        @Test
        void success() {
            // given
            Long productId = 1L;
            int stock = 50;
            int amount = 10;
            Product findProduct = Product.create(1L, "사과", stock, 5000);

            when(productRepository.find(productId)).thenReturn(Optional.of(findProduct));

            // when
            Product deductedProduct = productService.deductStock(productId, amount);

            // then
            assertThat(deductedProduct.getId()).isEqualTo(productId);
            assertThat(deductedProduct.getStock()).isEqualTo(stock - amount);
            verify(productRepository, times(1)).find(productId);
            verify(productRepository, times(1)).save(findProduct);
        }
    }


    private static Product makeProduct(Long productId, String name, int price, int stock) {
        return Product.create(productId, name, price, stock);
    }

}