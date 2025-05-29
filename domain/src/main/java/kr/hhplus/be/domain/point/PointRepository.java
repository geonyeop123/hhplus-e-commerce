package kr.hhplus.be.domain.point;

import java.util.Optional;

public interface PointRepository {
    Optional<Point> findByUserId(Long userId);

    Optional<Point> findByUserIdForUpdate(Long userId);
}
