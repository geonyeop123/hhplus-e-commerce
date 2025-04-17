package kr.hhplus.be.server.infra.point;

import kr.hhplus.be.server.domain.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaPointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByUserId(Long userId);
}
