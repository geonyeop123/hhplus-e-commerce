package kr.hhplus.be.server.domain.point;

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
public class PointConcurrencyTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    @DisplayName("동시에 충전과 사용을 요청하여도 정상적으로 수행된다.")
    @Test
    @Commit
    @Sql(scripts = "/sql/point.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void concurrencyUseAndCharge() throws InterruptedException {
        // given
        Long userId = 1L;
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            int index = i;
            executorService.submit(() -> {
                if (index % 2 == 0) {
                    pointService.use(new PointCommand.Use(userId, 10));
                } else {
                    pointService.charge(new PointCommand.Charge(userId, 10));
                }
                latch.countDown();
            });
        }

        latch.await(); // 모든 작업이 끝날 때까지 대기

        // then
        Point finalPoint = pointRepository.findByUserId(userId).orElseThrow();
        assertThat(finalPoint.getBalance()).isEqualTo(0); // 총합 결과
    }
}
