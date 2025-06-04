package kr.hhplus.be.infra.point;

import kr.hhplus.be.domain.point.Point;
import kr.hhplus.be.domain.point.PointRepository;
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

    @Override
    public Optional<Point> findByUserIdForUpdate(Long userId) {
        return jpaPointRepository.findByUserIdForUpdate(userId);
    }
}
