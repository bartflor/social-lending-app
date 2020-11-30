package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentSchedule;

import java.util.Optional;

public interface JpaScheduleRepository extends JpaRepository<RepaymentScheduleEntity, Long> {
	
}
