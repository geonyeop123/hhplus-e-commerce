package kr.hhplus.be.domain.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    public void saveDailyProducts(StatsCommand.NewSaveDailySalesProducts command) {
        statsRepository.saveAll(command.dailySalesProducts());
    }

}
