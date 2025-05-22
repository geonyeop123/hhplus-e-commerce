package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.common.PageResult;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.userCoupon.UserCouponCommand;
import kr.hhplus.be.server.domain.userCoupon.UserCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponController.class)
@ExtendWith(SpringExtension.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CouponFacade couponFacade;

    @MockitoBean
    private CouponService couponService;

    @MockitoBean
    private UserCouponService userCouponService;

    @MockitoBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.create("yeop");
        when(userService.findById(1L)).thenReturn(user);
    }

    @DisplayName("사용자와 쿠폰의 id를 통해 쿠폰을 발급받을 수 있다.")
    @Test
    void post_api_v1_users_userId_coupons_couponId_200() throws Exception{
        // given
//        Long userId = 1L;
//        Long couponId = 1L;
//        CouponCriteria.IssueUserCoupon criteria = new CouponCriteria.IssueUserCoupon(user, couponId);
//        CouponResult.IssueUserCoupon result  =new CouponResult.IssueUserCoupon(1L, "4월 반짝 쿠폰", 5000, LocalDate.of(2025, 4, 20));
//        when(couponFacade.issueUserCoupon(criteria)).thenReturn(result);
//
//        // when // then
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coupons/{couponId}", couponId)
//                        .header("X-USER-ID", userId))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userCouponId").value(result.id()))
//                .andExpect(jsonPath("$.name").value(result.name()))
//                .andExpect(jsonPath("$.discountAmount").value(result.discountAmount()))
//                .andExpect(jsonPath("$.expirationAt").value("2025-04-20"))
//        ;
    }

    @DisplayName("userId에 해당하는 사용자의 쿠폰 목록을 조회할 수 있다.")
    @Test
    void get_api_v1_users_userId_coupons_200() throws Exception{
        // given
        Long userId = 1L;
        UserCouponCommand.FindAll command = new UserCouponCommand.FindAll(user, 1, 10);
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(userId)
                .couponId(1L)
                .name("4월 반짝 쿠폰")
                .discountAmount(5000)
                .expiredAt(LocalDate.of(2025, 4, 20))
                .build();
        PageResult<UserCoupon> result = PageResult.create(List.of(userCoupon),
                command.pageNo(), command.pageSize(), 1);
        when(userCouponService.findAllByUserId(command)).thenReturn(result);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/coupons")
                        .header("X-USER-ID", userId)
                        .queryParam("pageNo", String.valueOf(command.pageNo()))
                        .queryParam("pageSize", String.valueOf(command.pageSize())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page").value(command.pageNo()))
                .andExpect(jsonPath("$.size").value(command.pageSize()))
                .andExpect(jsonPath("$.totalCount").value(result.totalCount()))
                .andExpect(jsonPath("$.totalPages").value(result.totalPages()))
        ;
    }

}