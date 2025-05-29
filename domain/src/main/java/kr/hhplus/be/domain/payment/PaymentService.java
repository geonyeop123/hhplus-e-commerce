package kr.hhplus.be.domain.payment;

import kr.hhplus.be.domain.point.PointCommand;
import kr.hhplus.be.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PointService pointService;

    public Payment pay(PaymentCommand.Pay command) {

        Payment payment = Payment.createByOrder(command.order());

        pointService.use(new PointCommand.Use(command.user(), payment.getTotalAmount()));
        payment.complete(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

}
