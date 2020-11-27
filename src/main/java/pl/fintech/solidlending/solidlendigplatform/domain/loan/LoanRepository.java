package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import java.util.List;
import java.util.Optional;

public interface LoanRepository {
	String LOAN_NOT_FOUND = "Loan with id:%s not found in repository";
	Long save(Loan loan);
	
	List<Loan> findAllByUsername(String userName);
	
	void setActive(Long loanId);
	
	Optional<Loan> findById(Long loanId);
	
	void update(Long loanId, Loan loan);
	
	void deleteAll();
	
	void delete(Long loanId);
}
