package kr.hhplus.be.interfaces.coupon;

import kr.hhplus.be.application.coupon.CouponCriteria;
import kr.hhplus.be.application.coupon.CouponFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(
        partitions   = 3,
        topics       = { "outside.coupon.issueCalled.v1" },
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        }
)
@TestPropertySource(properties = {
        // 컨슈머가 구독 시점 이전 메시지도 읽도록
        "spring.kafka.consumer.auto-offset-reset=earliest",
        // EmbeddedKafka가 제공하는 브로커 주소 사용
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
@EnableKafka
@AutoConfigureJsonTesters
class CouponListenerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private JacksonTester<CouponCriteria.IssueUserCoupon> json;

    @MockitoBean
    private CouponFacade couponFacade;

    @Test
    void listenerShouldCallFacade_whenMessageArrives() throws Exception {
        // 1) 보낼 메시지 준비
        Long userId = 1L;
        Long couponId = 100L;
        CouponCriteria.IssueUserCoupon command = new CouponCriteria.IssueUserCoupon(userId, couponId);
        String payload = json.write(command).getJson();

        // 2) 카프카 토픽에 메시지 발행
        kafkaTemplate.send("outside.coupon.issueCalled.v1", userId.toString(), payload);

        kafkaTemplate.flush();

        // 3) 비동기 리스너가 호출될 때까지 최대 5초 대기하며 couponFacade 호출 여부 검증
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        verify(couponFacade, times(1)).issueUserCoupon(command)
                );
    }
}