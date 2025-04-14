package kr.hhplus.be.server.domain.point;

import java.util.Optional;

public interface PointRepository {
    Optional<Point> findByUserId(Long userId);
    Point save(Point point);
    void deleteAllInBatch();
}
