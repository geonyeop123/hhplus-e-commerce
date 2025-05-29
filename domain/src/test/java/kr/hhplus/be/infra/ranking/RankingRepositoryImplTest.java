package kr.hhplus.be.infra.ranking;

import kr.hhplus.be.domain.order.OrderProduct;
import kr.hhplus.be.domain.product.ProductInfo;
import kr.hhplus.be.domain.ranking.RankingCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class RankingRepositoryImplTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private RedisRankingRepository redisRankingRepository;

    @DisplayName("saveDailyRanking 호출하면 파라미터로 들어온 orderProduct와 orderDate를 통해 key와 member를 생성하여 quantity만큼 score를 추가한다.")
    @Test
    void saveSalesProductsByOrder() {
        // given
        OrderProduct orderProduct1 = OrderProduct.create(new ProductInfo(1L, "사과", 1000, 10), 2);
        OrderProduct orderProduct2 = OrderProduct.create(new ProductInfo(2L, "배", 1000, 10), 1);
        RankingCommand.SaveSalesProduct command = new RankingCommand.SaveSalesProduct(List.of(orderProduct1, orderProduct2)
                , LocalDateTime.of(2025, 5, 12, 0, 0, 0));
        when(redisTemplate.opsForZSet().incrementScore(anyString(), anyString(), anyDouble())).thenReturn(1.0);
        // when
        redisRankingRepository.saveSalesProduct(command.orderProducts(), command.orderDateTime());

        // then
        verify(redisTemplate.opsForZSet(), times(2)).incrementScore(anyString(), anyString(), anyDouble());
    }

}