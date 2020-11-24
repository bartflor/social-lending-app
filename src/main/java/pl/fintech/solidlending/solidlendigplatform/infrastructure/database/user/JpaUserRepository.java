package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.User;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUserNameAndRole(String userName, UserEntity.Role role);
}
