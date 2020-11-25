package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import java.util.List;
import java.util.Optional;

public interface InvestmentRepository {
	Long save(Investment investment);
	
	List<Investment> findAllByUsername(String userName);
	
	void setActiveWithLoanId(Long loanId);
	
	void deleteAll();
	
	Optional<Investment> findById(Long investmentId);
}
