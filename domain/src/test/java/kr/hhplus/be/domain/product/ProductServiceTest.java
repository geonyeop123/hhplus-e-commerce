package kr.hhplus.be.domain.product;

import kr.hhplus.be.domain.common.PageResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
            Product req = makeProduct("사과", 5000, 50);
            when(productRepository.find(productId)).thenReturn(Optional.of(req));

            // when
            ProductInfo product = productService.find(productId);

            // then
            assertThat(product).isNotNull();
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
            ProductCommand.FindAll command = new ProductCommand.FindAll(1, 10);
            List<Product> content = List.of(
                    makeProduct("사과", 5000, 50),
                    makeProduct("배", 4000, 50)
            );
            Pageable pageable = PageRequest.of(0, 10);
            Page<Product> productPage = new PageImpl<>(content, pageable, 2);
            when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

            // when
            PageResult<ProductInfo> pageResult = productService.findAll(command);

            // then
            assertThat(pageResult.content()).hasSize(2);
            assertThat(pageResult.page()).isEqualTo(1);
            assertThat(pageResult.size()).isEqualTo(10);
            assertThat(pageResult.totalCount()).isEqualTo(2);
            assertThat(pageResult.totalPages()).isEqualTo(1);
            verify(productRepository, times(1)).findAll(any(Pageable.class));
        }
    }

    @Nested
    class validatePurchase {
        @DisplayName("구매가 가능한 상품인지 유효성 검사를 할 수 있다.")
        @Test
        void success() {
            // given
            Long productId = 1L;
            int stock = 50;
            int amount = 10;
            Product findProduct = Product.create("사과", stock, 5000);

            when(productRepository.find(productId)).thenReturn(Optional.of(findProduct));

            // when
            ProductCommand.ValidatePurchase command = new ProductCommand.ValidatePurchase(productId, amount);
            productService.validatePurchase(command);

            // then
            verify(productRepository, times(1)).find(productId);
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
            Product findProduct = Product.create("사과", stock, 5000);

            when(productRepository.findByIdForUpdate(productId)).thenReturn(Optional.of(findProduct));

            // when
            ProductInfo deductedProduct = productService.deductStock(new ProductCommand.DeductStock(productId, amount));

            // then
            assertThat(deductedProduct.stock()).isEqualTo(stock - amount);
            verify(productRepository, times(1)).findByIdForUpdate(productId);
        }
    }
    private static Product makeProduct(String name, int price, int stock) {
        return Product.create(name, price, stock);
    }

}