package kr.hhplus.be.server.domain.point;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;

    public Point find(Long userId) {
        return pointRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원입니다."));
    }

    @Transactional
    public Point charge(PointCommand.CHARGE command) {

        Point point = find(command.userId());
        point.charge(command.amount());

        return point;
    }

    public Point use(PointCommand.USE command) {
        Point point = find(command.userId());

        point.use(command.amount());
        return point;
    }
}
