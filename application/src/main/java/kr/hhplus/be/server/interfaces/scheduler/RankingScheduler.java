package kr.hhplus.be.server.interfaces.scheduler;

import kr.hhplus.be.domain.ranking.RankingCommand;
import kr.hhplus.be.domain.ranking.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RankingScheduler {

    private final RankingService rankingService;

    @Scheduled(cron = "0 0 0-23 * * *")
    public void hourlySalesProducts() {
        LocalDateTime now = LocalDateTime.now();
        RankingCommand.SaveDailyRanking command = new RankingCommand.SaveDailyRanking(now);
        rankingService.saveDailyRanking(command);
    }

}
