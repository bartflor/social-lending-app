package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;

import java.util.List;

@Repository
public interface JpaLoanRepository extends JpaRepository<LoanEntity, Long> {
	List<LoanEntity> findAllByBorrowerName(String userName);
}
