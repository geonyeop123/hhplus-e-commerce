package kr.hhplus.be.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long userId);
}