package kr.hhplus.be.server.interfaces.order;

import jakarta.validation.Valid;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.interfaces.common.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderDocs{

    private final OrderFacade orderFacade;

    @PostMapping("/api/v1/orders")
    public ResponseEntity<OrderResponse> order(@CurrentUser User user, @Valid @RequestBody OrderRequest.Create request) {
        OrderCriteria.Create criteria = request.toCriteria(user);
        OrderResult result = orderFacade.order(criteria);
        return ResponseEntity.ok(OrderResponse.from(result));
    }

}
