package kr.hhplus.be.domain.product;

import kr.hhplus.be.support.exception.NotEnoughStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Nested
    class deductStock {
        @DisplayName("재고를 감소시킬 수 있다.")
        @Test
        void success() {
            // given
            int deductAmount = 10;
            int stock = 50;
            Product product = Product.create( "사과", stock, 5000);

            // when
            product.deductStock(deductAmount);
            // then
            assertThat(product.getStock()).isEqualTo(stock - deductAmount);
        }

        @DisplayName("재고보다 많은 양을 감소시키는 경우 IllegalArgumentException이 발생한다.")
        @Test
        void fail() {
            // given
            int deductAmount = 2;
            int stock = 1;
            Product product = Product.create( "사과", stock, 5000);

            // when // then
            assertThatThrownBy(() -> product.deductStock(deductAmount))
                    .isInstanceOf(NotEnoughStockException.class)
                    .hasMessage("재고가 부족합니다.");
        }
    }

    @Nested
    class validatePurchasable {
        @DisplayName("재고가 없는 경우 IllegalArgumentException이 발생한다.")
        @Test
        void fail1() {
            // given
            int deductAmount = 2;
            int stock = 1;
            Product product = Product.create( "사과", stock, 5000);

            // when // then
            assertThatThrownBy(() -> product.validatePurchasable(deductAmount))
                    .isInstanceOf(NotEnoughStockException.class)
                    .hasMessage("재고가 부족합니다.");
        }
    }

}