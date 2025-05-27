package kr.hhplus.be.server.infra.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.common.GlobalEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements GlobalEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(String topic, Object key, Object payload) {
        try {
            String stringPayload = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, key.toString(), stringPayload);
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 실패: {}", e.getMessage());
        }
    }

    @Override
    public void publish(String topic, Object payload) {
        try {
            String stringPayload = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, stringPayload);
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 실패: {}", e.getMessage());
        }
    }
}
