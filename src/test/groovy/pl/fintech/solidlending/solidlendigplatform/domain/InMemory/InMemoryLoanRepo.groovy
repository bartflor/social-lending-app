package pl.fintech.solidlending.solidlendigplatform.domain.InMemory


import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRepository

import java.util.stream.Collectors

class InMemoryLoanRepo implements LoanRepository {
	private Map<Long, Loan> repo
	private static Long lastId
	
	static {
		lastId = 0l
	}
	
	InMemoryLoanRepo() {
		this.repo = new HashMap<>()
	}
	
	@Override
	Long save(Loan loan) {
		loan.setId(++lastId)
		repo.put(lastId, loan)
		return lastId
	}
	
	@Override
	List<Loan> findAllByUsername(String userName) {
		return repo.values().stream().filter(({ loan -> loan.getBorrowerUserName().equals(userName) }))
				.collect(Collectors.toList())
		
	}
	
	Optional<Loan> findById(Long loanId) {
		return Optional.ofNullable(repo.get(loanId))
	}
	
	@Override
	void update(Long loanId, Loan loan) {
		repo.put(loanId, loan)
	}
	
	@Override
	void deleteAll() {
		repo.clear()
	}

	@Override
	void delete(Long loanId) {

	}

	@Override
	void setActive(Long loanId) {
		Loan loan = findById(loanId)
				.orElseThrow({ -> new NoSuchElementException(String.format(LOAN_NOT_FOUND, loanId)) })
		loan.setStatus(Loan.LoanStatus.ACTIVE)
		repo.put(loanId, loan)
	}

}
