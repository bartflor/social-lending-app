package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent;

import java.util.Arrays;
import java.util.Collection;

public interface LoanApplicationService {
	Long createLoan(EndAuctionEvent auction);
	
	Long activateLoan(Long loanId);
	
	RepaymentSchedule getRepaymentScheduleByLoanId(Long loanId);
	
	String repayLoan(Long loanId);
	
	Loan findLoanById(Long loanId);
	
	Collection<Loan> getUserLoans(String userName);
	
	Collection<Investment> getUserInvestments(String userName);
}
