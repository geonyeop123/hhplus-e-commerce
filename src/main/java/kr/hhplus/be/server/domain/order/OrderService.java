package kr.hhplus.be.server.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order order(OrderCommand command) {
        Order order = Order.create(command.user());

        command.orderLines().stream()
                    .map(orderProduct ->
                            OrderProduct.create(orderProduct.product(), orderProduct.quantity()))
                    .forEach(order::addOrderProduct);

        order.calculateTotalAmount();

        return orderRepository.save(order);
    }

    public Order complete(Order order){
        order.complete();
        return orderRepository.save(order);
    }
}
