package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;

import java.util.Collection;

public interface LoanApplicationService {
	Long createLoan(EndAuctionEvent auction);
	
	Long activateLoan(Long loanId);
	
	void repayLoan(Long loanId);
	
	Loan findLoanById(Long loanId);
	
	Collection<Loan> getUserLoans(String userName);
	
	Collection<Investment> getUserInvestments(String userName);
	
	void giveOpinionOnBorrower(Opinion opinion);
	
	void rejectLoanProposal(Long loanId);
}
