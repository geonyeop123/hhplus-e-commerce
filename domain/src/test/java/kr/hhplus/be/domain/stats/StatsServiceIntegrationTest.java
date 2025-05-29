package kr.hhplus.be.domain.stats;

import kr.hhplus.be.infra.stats.JpaStatsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create"
})
@Transactional
class StatsServiceIntegrationTest {

    @Autowired
    private StatsService statsService;

    @Autowired
    private JpaStatsRepository jpaStatsRepository;

    @DisplayName("일간 판매 데이터를 받아 저장할 수 있다.")
    @Test
    void saveDailyProducts() {
        // given
        List<DailySalesProduct> list = List.of(DailySalesProduct.create(1L, 10, LocalDate.now()), DailySalesProduct.create(2L, 10, LocalDate.now()));
        StatsCommand.NewSaveDailySalesProducts command = new StatsCommand.NewSaveDailySalesProducts(list);
        // when
        statsService.saveDailyProducts(command);

        // then
        List<DailySalesProduct> all = jpaStatsRepository.findAll();
        assertThat(all).hasSize(2);
        assertThat(all).extracting("productId", "salesCount", "orderDate")
                .containsExactlyInAnyOrder(tuple(1L, 10, LocalDate.now()), tuple(2L, 10, LocalDate.now()));
    }

}