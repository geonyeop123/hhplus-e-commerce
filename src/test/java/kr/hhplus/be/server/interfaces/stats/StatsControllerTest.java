package kr.hhplus.be.server.interfaces.stats;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
class StatsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("통계 요청 시 3일간 가장 많이 판매된 5개의 상품을 반환한다.")
    @Test
    void get_api_v1_stats_products_popular_200()throws Exception {
        // given // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stats/products/popular"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5))
                ;
    }

}