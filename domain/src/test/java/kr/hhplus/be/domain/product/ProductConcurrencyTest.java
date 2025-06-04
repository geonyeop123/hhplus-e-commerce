package kr.hhplus.be.domain.product;

import kr.hhplus.be.infra.product.JpaProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create"
})
class ProductConcurrencyTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @AfterEach
    void tearDown() {
        jpaProductRepository.deleteAllInBatch();
    }

    @DisplayName("동시에 재고차감이 이루어져도 정상적으로 재고가 차감된다.")
    @Test
    void concurrencyDeductStock() throws InterruptedException {
        // given
        Product product = productRepository.save(Product.create("사과", 10, 1000));
        Long productId = product.getId();

        int threadCount = 12;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try{
                    ProductCommand.DeductStock command = new ProductCommand.DeductStock(productId, 1);
                    productService.deductStock(command);
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 작업이 끝날 때까지 대기

        // then
        Product finalproduct = productRepository.find(productId).orElse(null);
        assertThat(finalproduct).isNotNull();
        assertThat(finalproduct.getStock()).isEqualTo(0);
    }

}