package kr.hhplus.be.server.application.order;


import kr.hhplus.be.server.domain.order.DiscountInfo;
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
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.userCoupon.UserCouponCommand;
import kr.hhplus.be.server.domain.userCoupon.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final UserCouponService userCouponService;
    private final OrderService orderService;
    private final PaymentService paymentService;


    public OrderResult order(OrderCriteria.Create criteria) {

        User user = userService.findByUserId(criteria.userId());

        DiscountInfo discountInfo = Optional.ofNullable(criteria.userCouponId())
                .map(userCouponId -> {
                    UserCoupon userCoupon = userCouponService.validate(criteria.userId(), criteria.userCouponId());
                    return DiscountInfo.from(userCoupon);
                })
                .orElse(DiscountInfo.empty());

        List<OrderCommand.OrderLine> orderLines = criteria.orderLines().stream()
                .map(orderLine -> {
                    Product product = productService.validatePurchase(orderLine.productId(), orderLine.quantity());
                    return new OrderCommand.OrderLine(product, orderLine.quantity());
                }).toList();

        OrderCommand orderCommand = new OrderCommand(user, discountInfo, orderLines);
        Order order = orderService.order(orderCommand);

        PaymentCommand paymentCommand = new PaymentCommand(order, criteria.userId());
        Payment payment = paymentService.pay(paymentCommand);
        Order completedOrder = orderService.complete(order);

        order.getOrderProducts()
                .forEach(orderProduct ->
                        productService.deductStock(orderProduct.getProduct().getId(), orderProduct.getQuantity()));

        Optional.ofNullable(criteria.userCouponId())
                .ifPresent(id -> {
                    UserCouponCommand.Use command = new UserCouponCommand.Use(criteria.userId(), criteria.userCouponId(), order.getId());
                    userCouponService.use(command);
                });

        return new OrderResult(
                completedOrder.getId(), payment.getId(),
                completedOrder.getOrderAmount(), payment.getTotalAmount());
    }

}
