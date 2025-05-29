package kr.hhplus.be.infra.user;

import kr.hhplus.be.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long> {
}
