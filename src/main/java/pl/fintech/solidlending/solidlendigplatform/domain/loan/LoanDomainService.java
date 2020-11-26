package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;

import java.util.List;
import java.util.Optional;

public interface LoanDomainService {
	Long createLoan(NewLoanParams params);
	
	Long activateLoan(Long loanId);
	
	void reportRepayment(Long loanId);
	
	Optional<Repayment> findNextRepayment(Long loanId);
	
	Loan findLoanById(Long loanId);
	
	List<Loan> getUserLoans(String userName);
	
	List<Investment> getUserInvestments(String userName);
	
	void giveOpinionOnBorrower(Opinion opinion);
}
