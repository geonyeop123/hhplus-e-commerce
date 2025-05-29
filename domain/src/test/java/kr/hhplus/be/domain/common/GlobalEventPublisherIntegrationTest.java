package kr.hhplus.be.domain.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create",
        "spring.kafka.consumer.group-id=my-group",
        "spring.kafka.consumer.auto-offset-reset=earliest",
})
@EmbeddedKafka(
        partitions = 1,
        topics = { "test.topic" },
        bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
@EnableKafka
class GlobalEventPublisherIntegrationTest {

    @Autowired
    private GlobalEventPublisher globalEventPublisher;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ConsumerFactory<String, String> consumerFactory;

    @Autowired
    ObjectMapper objectMapper;

    private Consumer<String, String> consumer;

    private static AdminClient adminClient;

    private static final String TEST_TOPIC = "test.topic";



    @BeforeEach
    void setup() {
        // EmbeddedKafka가 주입할 spring.kafka.bootstrap-servers 프로퍼티를 사용
        String bootstrapServers = System.getProperty("spring.kafka.bootstrap-servers");
        adminClient = AdminClient.create(Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers
        ));
    }

    @AfterEach
    void tearDown() {
        adminClient.deleteTopics(List.of(TEST_TOPIC));
    }

    static class TestValue {
        public String value;
        public TestValue(String value) {
            this.value = value;
        }
        public TestValue() {}
    }

    @DisplayName("이벤트를 publish할 시 토픽이 생성된다.")
    @Test
    void publishEvent() throws JsonProcessingException {
        // given
        String key = "1";
        TestValue value = new TestValue("test");
        consumer = consumerFactory.createConsumer();
        consumer.subscribe(List.of(TEST_TOPIC));

        // when
        globalEventPublisher.publish(TEST_TOPIC, key, value);

        // KafkaTemplate은 비동기이므로 flush로 즉시 동기화
        kafkaTemplate.flush();

        // then
        ConsumerRecord<String, String> record = KafkaTestUtils.getRecords(consumer).iterator().next();
        assertThat(record.topic()).isEqualTo(TEST_TOPIC);
        assertThat(record.key()).isEqualTo(key);
        TestValue readValue = objectMapper.readValue(record.value(), TestValue.class);
        assertThat(readValue.value).isEqualTo(value.value);
    }

}