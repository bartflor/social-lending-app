package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaInvestmentRepository extends JpaRepository<InvestmentEntity, Long> {
	
	List<InvestmentEntity> findAllByLenderName(String userName);
	
	List<InvestmentEntity> findAllByLoanId(Long loanId);
}
