package kr.hhplus.be.domain.point;

import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.infra.point.JpaPointRepository;
import kr.hhplus.be.infra.user.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create"
})
@Transactional
class PointServiceIntegrationTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private JpaPointRepository jpaPointRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @DisplayName("userId로 해당 유저가 보유한 포인트를 조회할 수 있다.")
    @Test
    void find() {
        // given
        User savedUser = jpaUserRepository.save(User.create("user"));
        jpaPointRepository.save(Point.create(savedUser, 0));
        // when
        Point findPoint = pointService.find(savedUser);
        // then
        assertThat(findPoint.getBalance()).isEqualTo(0);
    }

    @DisplayName("userId와 amount로 해당하는 유저의 포인트를 충전할 수 있다.")
    @Test
    void charge() {
        // given
        User savedUser = jpaUserRepository.save(User.create("user"));
        jpaPointRepository.save(Point.create(savedUser, 0));
        int amount = 10;
        PointCommand.Charge command = new PointCommand.Charge(savedUser, amount);

        // when
        Point chargedPoint = pointService.charge(command);

        // then
        assertThat(chargedPoint.getBalance()).isEqualTo(amount);
    }

    @DisplayName("userId와 amount로 해당하는 유저의 포인트를 사용할 수 있다.")
    @Test
    void use() {
        // given
        User savedUser = jpaUserRepository.save(User.create("user"));
        jpaPointRepository.save(Point.create(savedUser, 10));
        int amount = 10;
        PointCommand.Use command = new PointCommand.Use(savedUser, amount);
        // when
        Point usedPoint = pointService.use(command);

        // then
        assertThat(usedPoint.getBalance()).isEqualTo(0);
    }

}