package kr.hhplus.be.server.application.order;


import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentCommand;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final PaymentService paymentService;


    public OrderResult order(OrderCriteria.Create criteria) {

        User user = userService.findByUserId(criteria.userId());

        List<OrderCommand.OrderLine> orderLines = criteria.orderLines().stream()
                .map(orderLine -> {
                    Product product = productService.validatePurchase(orderLine.productId(), orderLine.quantity());
                    return new OrderCommand.OrderLine(product, orderLine.quantity());
                }).toList();

        OrderCommand orderCommand = new OrderCommand(user, orderLines);
        Order order = orderService.order(orderCommand);

        PaymentCommand paymentCommand = new PaymentCommand(order, criteria.userId());
        Payment payment = paymentService.pay(paymentCommand);
        Order completedOrder = orderService.complete(order);

        order.getOrderProducts()
                .forEach(orderProduct -> 
                        productService.deductStock(orderProduct.getProduct().getId(), orderProduct.getQuantity()));

        return new OrderResult(
                completedOrder.getId(), payment.getId(),
                completedOrder.getTotalOrderAmount(), payment.getTotalAmount());
    }

}
