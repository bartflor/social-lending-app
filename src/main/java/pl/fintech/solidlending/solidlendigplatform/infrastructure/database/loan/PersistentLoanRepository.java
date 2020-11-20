package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PersistentLoanRepository implements LoanRepository {
	
	JpaLoanRepository jpaLoanRepository;
	
	@Override
	public Long save(Loan loan) {
		return jpaLoanRepository.save(LoanEntity.from(loan)).getId();
	}
	
	@Override
	public List<Loan> findAllByUsername(String userName) {
		return jpaLoanRepository.findAllByBorrowerName(userName).stream()
				.map(LoanEntity::toDomain)
				.collect(Collectors.toList());
	}
	
	@Override
	public void setActive(Long loanId) {
		LoanEntity loan = jpaLoanRepository.findById(loanId)
				.orElseThrow(() -> new LoanNotFoundException(String.format(LOAN_NOT_FOUND,loanId)));
		loan.setStatus(Loan.LoanStatus.ACTIVE);
		jpaLoanRepository.save(loan);
	}
	
	@Override
	public Optional<Loan> findById(Long loanId) {
		return jpaLoanRepository.findById(loanId).map(LoanEntity::toDomain);
	}
	
	@Override
	public void update(Long loanId, Loan loan) {
		loan.setId(loanId);
		jpaLoanRepository.save(LoanEntity.from(loan));
	}
}
