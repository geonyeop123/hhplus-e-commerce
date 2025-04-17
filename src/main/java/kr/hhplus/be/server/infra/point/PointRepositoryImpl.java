package kr.hhplus.be.server.infra.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final JpaPointRepository jpaPointRepository;

    @Override
    public Optional<Point> findByUserId(Long userId) {
        return jpaPointRepository.findByUserId(userId);
    }
}
