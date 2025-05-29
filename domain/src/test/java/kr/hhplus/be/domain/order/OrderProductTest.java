package kr.hhplus.be.domain.order;


import kr.hhplus.be.domain.product.Product;
import kr.hhplus.be.domain.product.ProductInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderProductTest {

    @DisplayName("create로 생성할 시 orderProduct의 price는 파라미터 product의 price와 동일한 값을 가진다.")
    @Test
    void createOrderProduct() {
        // given
        ProductInfo productInfo = ProductInfo.from(Product.create( "사과", 50, 5000));

        // when
        OrderProduct orderProduct = OrderProduct.create(productInfo, 1);

        // then
        assertThat(orderProduct.getPrice()).isEqualTo(productInfo.price());
        assertThat(orderProduct.getName()).isEqualTo(productInfo.name());
    }

}