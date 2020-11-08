package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;

import java.util.Arrays;
import java.util.Collection;

public interface LoanApplicationService {
	Long createLoan(Auction auction);
	
	Long activateLoan(Long loanId);
	
	Loan findLoanById(Long loanId);
	
	Collection<Loan> getUserLoans(String userName);
	
	Collection<Investment> getUserInvestments(String userName);
}
