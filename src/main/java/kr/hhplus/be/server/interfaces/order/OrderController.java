package kr.hhplus.be.server.interfaces.order;

import jakarta.validation.Valid;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
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
    public ResponseEntity<OrderResponse> order(@Valid @RequestBody OrderRequest.Create request) {
        OrderCriteria.Create criteria = request.toCriteria();
        OrderResult result = orderFacade.order(criteria);
        return ResponseEntity.ok(OrderResponse.from(result));
    }

}
