package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentSchedule;

import java.util.Optional;

@Repository
public interface JpaScheduleRepository extends JpaRepository<RepaymentScheduleEntity, Long> {
	
	 Optional<RepaymentScheduleEntity> findByOwnerIdAndTypeEquals(Long loanId, RepaymentSchedule.Type type);
}
