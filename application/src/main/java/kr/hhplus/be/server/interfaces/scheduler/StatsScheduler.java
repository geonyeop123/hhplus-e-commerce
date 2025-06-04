package kr.hhplus.be.server.interfaces.scheduler;

import kr.hhplus.be.server.application.stats.StatsCriteria;
import kr.hhplus.be.server.application.stats.StatsFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class StatsScheduler {

    private final StatsFacade statsFacade;

    @Scheduled(cron = "0 0 0 * * *")
    public void saveDailyRanking() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        StatsCriteria criteria = new StatsCriteria(yesterday);
        statsFacade.saveDailySalesProductStats(criteria);
    }

}
