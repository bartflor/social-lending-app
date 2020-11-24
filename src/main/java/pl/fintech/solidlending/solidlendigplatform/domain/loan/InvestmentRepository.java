package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import java.util.List;

public interface InvestmentRepository {
	Long save(Investment investment);
	
	List<Investment> findAllByUsername(String userName);
	
	void setActiveWithLoanId(Long loanId);
	
	void deleteAll();
}
