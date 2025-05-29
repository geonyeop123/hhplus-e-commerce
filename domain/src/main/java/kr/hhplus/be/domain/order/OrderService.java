package kr.hhplus.be.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher publisher;

    public Order order(OrderCommand.Create command) {
        Order order = Order.create(command.user());

        command.orderLines().stream()
                    .map(orderLine -> OrderProduct.create(orderLine.product(), orderLine.quantity()))
                    .forEach(order::addOrderProduct);

        order.applyCoupon(command.userCouponInfo());
        order.complete();

        publisher.publishEvent(new OrderEvent.Completed(OrderInfo.from(order)));

        return orderRepository.save(order);
    }
}
