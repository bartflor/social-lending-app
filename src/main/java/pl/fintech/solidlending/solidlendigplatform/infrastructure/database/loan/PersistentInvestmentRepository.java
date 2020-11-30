package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Investment;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.InvestmentRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Transactional
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
	
	@Override
	public void deleteAll(){
		jpaInvestmentRepository.deleteAll();
	}
	
	@Override
	public Optional<Investment> findById(Long investmentId) {
		return jpaInvestmentRepository.findById(investmentId)
				.map(InvestmentEntity::toDomain);
	}
}
