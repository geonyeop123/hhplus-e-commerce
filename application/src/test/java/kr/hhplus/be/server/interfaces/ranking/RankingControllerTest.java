package kr.hhplus.be.server.interfaces.ranking;

import kr.hhplus.be.domain.ranking.Ranking;
import kr.hhplus.be.domain.ranking.RankingType;
import kr.hhplus.be.domain.ranking.SalesProduct;
import kr.hhplus.be.domain.user.UserService;
import kr.hhplus.be.server.application.ranking.RankingFacade;
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

@WebMvcTest(RankingController.class)
class RankingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private RankingFacade rankingFacade;

    @MockitoBean
    private UserService userService;

    @DisplayName("일간 판매 순위 조회 시 판매 순으로 상품이 반환된다.")
    @Test
    void get_api_v1_ranking_daily_200()throws Exception {
        // given
        List<SalesProduct> products = List.of(
                new SalesProduct(1L, "상품1", 1000, 10, 10),
                new SalesProduct(2L, "상품2", 1000, 10, 10),
                new SalesProduct(3L, "상품3", 1000, 10, 10),
                new SalesProduct(4L, "상품4", 1000, 10, 10),
                new SalesProduct(5L, "상품5", 1000, 10, 10)
        );

        when(rankingFacade.findDailyRankingProducts()).thenReturn(new Ranking(RankingType.DAILY, products));


        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ranking/daily"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5))
        ;
    }

}