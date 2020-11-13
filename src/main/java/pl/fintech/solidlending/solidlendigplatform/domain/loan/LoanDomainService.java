package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import java.util.List;

public interface LoanDomainService {
	Long createLoan(LoanParams params);
	
	Long activateLoan(Long loanId);
	
	String repay(Long loanId);
	
	Repayment findNextRepayment(Long loanId);
	
	Loan findLoanById(Long loanId);
	
	List<Loan> getUserLoans(String userName);
	
	List<Investment> getUserInvestments(String userName);
	
	RepaymentSchedule findLoanRepaymentSchedule(Long loanId);
}
