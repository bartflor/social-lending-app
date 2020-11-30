package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface JpaLoanRepository extends JpaRepository<LoanEntity, Long> {
	List<LoanEntity> findAllByBorrowerName(String userName);
}
