package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface LoanDomainService {
	Long createLoan(NewLoanParams params);
	
	Long activateLoan(Long loanId);
	
	boolean checkLoanStatus(Long loanId, Loan.LoanStatus status);
	
	void reportRepayment(Long loanId);
	
	Set<Investment> getLoanInvestmentsForRepayment(Long loanId);
	
	Loan findLoanById(Long loanId);
	
	List<Loan> getUserLoans(String userName);
	
	List<Investment> getUserInvestments(String userName);
	
	void giveOpinionOnBorrower(Opinion opinion);
	
	void rejectLoan(Long loanId);
}
