package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Investment;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.InvestmentRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PersistentInvestmentRepository implements InvestmentRepository {
	JpaInvestmentRepository jpaInvestmentRepository;
	
	@Override
	public Long save(Investment investment) {
		return jpaInvestmentRepository.save(InvestmentEntity.from(investment)).getInvestmentId();
	}
	
	@Override
	public List<Investment> findAllByUsername(String userName) {
		return jpaInvestmentRepository.findAllByLenderName(userName).stream()
				.map(InvestmentEntity::toDomain)
				.collect(Collectors.toList());
				
	}
	
	@Override
	public void setActiveWithLoanId(Long loanId) {
		jpaInvestmentRepository.findAllByLoanId(loanId)
				.forEach(investmentEntity -> {investmentEntity.setStatus(Investment.Status.ACTIVE);
													jpaInvestmentRepository.save(investmentEntity);});
	}
}
