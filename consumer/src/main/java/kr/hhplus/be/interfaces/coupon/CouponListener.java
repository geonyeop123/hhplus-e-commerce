package kr.hhplus.be.interfaces.coupon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.application.coupon.CouponCriteria;
import kr.hhplus.be.application.coupon.CouponFacade;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponListener {

    private final CouponFacade couponFacade;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "outside.coupon.issueCalled.v1",
            groupId = "issue",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(
            ConsumerRecord<String, String> record,
            Acknowledgment ack) throws JsonProcessingException {

        // 1) 처리 로직
        CouponCriteria.IssueUserCoupon issueUserCoupon =
                objectMapper.readValue(record.value(), CouponCriteria.IssueUserCoupon.class);

        couponFacade.issueUserCoupon(issueUserCoupon);

        // 2) 오프셋 커밋
        ack.acknowledge();
    }

}
