package kr.hhplus.be.server.application.order;


import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentCommand;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.userCoupon.UserCouponCommand;
import kr.hhplus.be.server.domain.userCoupon.UserCouponInfo;
import kr.hhplus.be.server.domain.userCoupon.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final UserCouponService userCouponService;
    private final OrderService orderService;
    private final PaymentService paymentService;


    @Transactional
    public OrderResult order(OrderCriteria.Create criteria) {

        User user = userService.findById(criteria.userId());

        UserCouponCommand.Validate couponCommand = new UserCouponCommand.Validate(criteria.userId(), criteria.userCouponId());
        UserCouponInfo userCouponInfo = userCouponService.validateAndGetInfo(couponCommand);

        List<OrderCommand.OrderLine> orderLines = criteria.orderItems().stream()
                .map(orderItem -> {
                    ProductInfo productInfo = productService.validatePurchase(orderItem.toValidateCommand());
                    return new OrderCommand.OrderLine(productInfo, orderItem.quantity());
                }).toList();

        OrderCommand.Create orderCommand = new OrderCommand.Create(user, userCouponInfo, orderLines);
        Order order = orderService.order(orderCommand);

        PaymentCommand.Pay paymentCommand = new PaymentCommand.Pay(order, criteria.userId());
        Payment payment = paymentService.pay(paymentCommand);

        criteria.orderItems().forEach(orderItem -> productService.deductStock(orderItem.toDeductCommand()));

        UserCouponCommand.Use command = new UserCouponCommand.Use(criteria.userId(), criteria.userCouponId());
        userCouponService.use(command);

        return new OrderResult(
                order.getId(), payment.getId(),
                order.getOrderAmount(), payment.getTotalAmount());
    }

}
