package kr.hhplus.be.domain.point;


import kr.hhplus.be.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    @Transactional(readOnly = true)
    public Point find(User user) {
        return pointRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원입니다."));
    }

//    @DistributedLock(topic = "point", key = "#command.user.id")
    @Transactional
    public Point charge(PointCommand.Charge command) {
        User user = command.user();
        Point point = pointRepository.findByUserIdForUpdate(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원입니다."));
        point.charge(command.amount());

        return point;
    }

//    @DistributedLock(topic = "point", key = "#command.user.id")
    @Transactional
    public Point use(PointCommand.Use command) {
        User user = command.user();
        Point point = pointRepository.findByUserIdForUpdate(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원입니다."));

        point.use(command.amount());
        return point;
    }
}
