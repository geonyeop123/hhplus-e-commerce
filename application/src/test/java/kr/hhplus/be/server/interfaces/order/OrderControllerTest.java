package kr.hhplus.be.server.interfaces.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.user.UserService;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.support.config.JpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = OrderController.class,
        excludeAutoConfiguration = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
        , excludeFilters = @ComponentScan.Filter(
        type  = FilterType.ASSIGNABLE_TYPE,
        classes = JpaConfig.class
        )
)
class OrderControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderFacade orderFacade;

    @MockitoBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.create("yeop");
        when(userService.findById(1L)).thenReturn(user);
    }

    @Test
    @DisplayName("정상적인 요청으로 주문/결제를 요청했을 때 200을 반환된다.")
    void post_api_v1_orders_200() throws Exception {
        // given
        OrderRequest.Create req = new OrderRequest.Create( 1L, List.of(new OrderRequest.OrderItem(1L, 1)));
        OrderCriteria.Create criteria = req.toCriteria(user);
        OrderResult result = new OrderResult(1L, 1L, 5000, 3000);
        when(orderFacade.order(criteria)).thenReturn(result);

        // when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                        .header("X-USER-ID", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.paymentId").value(1L))
                .andExpect(jsonPath("$.orderTotalAmount").value(5000))
                .andExpect(jsonPath("$.paymentTotalAmount").value(3000))
        ;
    }


}