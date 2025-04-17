package kr.hhplus.be.server.domain.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    @Transactional(readOnly = true)
    public List<PopularProduct> getPopularProducts() {

        return statsRepository.getPopularProducts();

    }

}
