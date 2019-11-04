package es.geeksusma.tddexample.user;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findById(Long userId);
}
