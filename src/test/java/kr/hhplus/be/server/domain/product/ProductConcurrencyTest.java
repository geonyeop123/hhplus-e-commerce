package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductConcurrencyTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("동시에 재고차감이 이루어져도 정상적으로 재고가 차감된다.")
    @Test
    @Commit
    @Sql(scripts = "/sql/product.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void concurrencyDeductStock() throws InterruptedException {
        // given
        Long productId = 1L;
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                ProductCommand.DeductStock command = new ProductCommand.DeductStock(productId, 1);
                productService.deductStock(command);
                latch.countDown();
            });
        }

        latch.await(); // 모든 작업이 끝날 때까지 대기

        // then
        Product finalproduct = productRepository.find(productId).orElse(null);
        assertThat(finalproduct).isNotNull();
        assertThat(finalproduct.getStock()).isEqualTo(0);
    }

}