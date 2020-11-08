package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;

import java.util.List;
import java.util.Optional;

public interface LoanRepository {
	Long save(Loan loan);
	
	List<Loan> findAllByUsername(String userName);
	
	void setActive(Long loanId);
	
	Optional<Loan> findById(Long loanId);
}
