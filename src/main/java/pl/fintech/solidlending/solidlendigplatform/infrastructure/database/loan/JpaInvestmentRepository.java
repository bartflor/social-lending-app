package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Investment;

import java.util.List;

public interface JpaInvestmentRepository extends JpaRepository<InvestmentEntity, Long> {
	
	List<InvestmentEntity> findAllByLenderName(String userName);
	
	List<InvestmentEntity> findAllByLoanId(Long loanId);
}
