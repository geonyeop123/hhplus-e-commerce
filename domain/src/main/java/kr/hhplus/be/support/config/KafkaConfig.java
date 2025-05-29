package kr.hhplus.be.support.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic couponIssueCalledTopic() {
        return TopicBuilder.name("outside.coupon.issueCalled.v1")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderCompletedTopic() {
        return TopicBuilder.name("outside.order.completed.v1")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
