package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryLoanRepo implements LoanRepository {
	private Map<Long, Loan> repo;
	private static Long lastId;
	
	static {
		lastId = 0l;
	}
	
	public InMemoryLoanRepo() {
		this.repo = new HashMap<>();
	}
	
	@Override
	public Long save(Loan loan) {
		loan.setId(++lastId);
		repo.put(lastId, loan);
		return lastId;
	}
	
	@Override
	public List<Loan> findAllByUsername(String userName) {
		return repo.values().stream().filter((loan -> loan.getBorrowerUserName().equals(userName)))
				.collect(Collectors.toList());
		
	}
	
	public Optional<Loan> findById(Long loanId) {
		return Optional.ofNullable(repo.get(loanId));
	}
	
	@Override
	public void setActive(Long loanId) {
		Loan loan = findById(loanId)
				.orElseThrow(NoSuchElementException::new); //TODO: custom exception?
		loan.setStatus(Loan.LoanStatus.ACTIVE);
		repo.put(loanId, loan);
	}

}
