package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.interfaces.coupon.response.CouponResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private CouponResponse couponResponse;

    @BeforeEach
    void setUp() {
        couponResponse = new CouponResponse(1L, 1L,"4월 깜짝 할인 쿠폰", 10000, LocalDate.of(2025,5,4), null);
    }

    @DisplayName("사용자와 쿠폰의 id를 통해 쿠폰을 발급받을 수 있다.")
    @Test
    void post_api_v1_users_userId_coupons_couponId_200() throws Exception{
        // given
        Long userId = couponResponse.userCouponId();
        Long couponId = couponResponse.userCouponId();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/{userId}/coupons/{couponId}", userId, couponId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.userCouponId").value(couponResponse.userCouponId()))
                .andExpect(jsonPath("$.name").value(couponResponse.name()))
                .andExpect(jsonPath("$.discountAmount").value(couponResponse.discountAmount()))
                .andExpect(jsonPath("$.expirationAt").value("2025-05-04"))
                .andExpect(jsonPath("$.usedAt").value(couponResponse.usedAt()))
        ;
    }

    @DisplayName("userId에 해당하는 사용자의 쿠폰 목록을 조회할 수 있다.")
    @Test
    void get_api_v1_users_userId_coupons_200() throws Exception{
        // given
        Long userId = couponResponse.userCouponId();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}/coupons", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].userId").value(userId))
                .andExpect(jsonPath("$.[0].userCouponId").value(couponResponse.userCouponId()))
                .andExpect(jsonPath("$.[0].name").value(couponResponse.name()))
                .andExpect(jsonPath("$.[0].discountAmount").value(couponResponse.discountAmount()))
                .andExpect(jsonPath("$.[0].expirationAt").value("2025-05-04"))
                .andExpect(jsonPath("$.[0].usedAt").value(couponResponse.usedAt()))
        ;
    }

}