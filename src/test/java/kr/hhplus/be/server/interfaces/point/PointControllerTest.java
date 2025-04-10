package kr.hhplus.be.server.interfaces.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.point.ChargeCommand;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
@ExtendWith(MockitoExtension.class)
class PointControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PointService pointService;

    @Nested
    class get_api_v1_points {
        @Test
        @DisplayName("정상적인 user id로 포인트를 조회했을 때 user의 보유한 포인트가 반환된다.")
        void success() throws Exception {
            // given
            Long userId = 1L;
            Point point = createPoint(userId, 0);

            when(pointService.find(userId)).thenReturn(point);

            // when //then
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}/points", userId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(userId))
                    .andExpect(jsonPath("$.balance").value(0))
            ;

            verify(pointService, times(1)).find(userId);
        }
    }

    @Nested
    class post_api_v1_points {
        @Test
        @DisplayName("정상적인 userId와 값으로 포인트를 충전했을 때 user의 충전 후 포인트가 반환된다.")
        void success() throws Exception {
            // given
            Long userId = 1L;
            int amount = 10;

            ChargeCommand command = new ChargeCommand(userId, amount);

            Point chargedPoint = createPoint(userId, amount);

            PointRequest.Charge request = new PointRequest.Charge(amount);

            when(pointService.charge(command)).thenReturn(chargedPoint);


            //when //then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/{userId}/points", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.userId").value(userId))
                        .andExpect(jsonPath("$.balance").value(amount))
            ;

            verify(pointService, times(1)).charge(command);
        }
    }

    private Point createPoint(Long userId, int balance){
        return Point.create(User.create(userId, "yeop"), balance);
    }
}