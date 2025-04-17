package kr.hhplus.be.server.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order order(OrderCommand.Create command) {
        Order order = Order.create(command.user());

        command.orderLines().stream()
                    .map(orderLine -> OrderProduct.create(orderLine.product(), orderLine.quantity()))
                    .forEach(order::addOrderProduct);

        order.applyCoupon(command.userCouponInfo());
        order.complete();

        return orderRepository.save(order);
    }
}
