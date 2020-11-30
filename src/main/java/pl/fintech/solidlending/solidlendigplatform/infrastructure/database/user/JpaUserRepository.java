package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUserNameAndRole(String userName, UserEntity.Role role);
}
