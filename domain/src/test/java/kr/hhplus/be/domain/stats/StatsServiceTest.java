package kr.hhplus.be.domain.stats;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private StatsRepository statsRepository;

    @InjectMocks
    private StatsService statsService;

    @DisplayName("일간 판매 데이터를 받아 저장할 때 statsRepository를 활용하여 저장한다.")
    @Test
    void saveDailyProducts() {
        // given
        List<DailySalesProduct> products = List.of(
                DailySalesProduct.create(1L, 10, LocalDate.now()),
                DailySalesProduct.create(2L, 20, LocalDate.now()),
                DailySalesProduct.create(3L, 30, LocalDate.now()),
                DailySalesProduct.create(4L, 40, LocalDate.now()),
                DailySalesProduct.create(5L, 50, LocalDate.now()));

        doNothing().when(statsRepository).saveAll(products);
        StatsCommand.NewSaveDailySalesProducts command = new StatsCommand.NewSaveDailySalesProducts(products);

        // when
        statsService.saveDailyProducts(command);

        // then
        verify(statsRepository, times(1)).saveAll(products);
    }

}