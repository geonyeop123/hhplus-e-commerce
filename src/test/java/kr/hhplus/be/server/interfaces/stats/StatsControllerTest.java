package kr.hhplus.be.server.interfaces.stats;

import kr.hhplus.be.server.domain.stats.PopularProduct;
import kr.hhplus.be.server.domain.stats.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
class StatsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private StatsService statsService;

    @DisplayName("통계 요청 시 3일간 가장 많이 판매된 5개의 상품을 반환한다.")
    @Test
    void get_api_v1_stats_products_popular_200()throws Exception {
        // given
        List<PopularProduct> products = List.of(
                new PopularProduct(1L, 1000L, "상품1", 1000, 10),
                new PopularProduct(2L, 1000L, "상품2", 1000, 10),
                new PopularProduct(3L, 1000L, "상품3", 1000, 10),
                new PopularProduct(4L, 1000L, "상품4", 1000, 10),
                new PopularProduct(5L, 1000L, "상품5", 1000, 10)
        );
        when(statsService.getPopularProducts()).thenReturn(products);


        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stats/products/popular"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products.length()").value(5))
                ;
    }

}