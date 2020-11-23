package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory;

import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Investment;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.InvestmentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class InMemoryInvestmentRepo implements InvestmentRepository {
		private Map<Long, Investment> repo;
		private static Long lastId;
		
		static {
			lastId = 0l;
		}
	
	public InMemoryInvestmentRepo() {
			this.repo = new HashMap<>();
		}
		
		public Long save(Investment investment) {
			investment.setInvestmentId(++lastId);
			repo.put(lastId, investment);
			return lastId;
		}
		
		@Override
		public List<Investment> findAllByUsername(String userName) {
			return repo.values().stream().filter(({ investment -> investment.getLenderName().equals(userName) }))
					.collect(Collectors.toList());
		}
	
	public List<Investment> findWithLoanId(Long loanId) {
			return repo.values().stream()
					.filter({ investment -> investment.getLoanId() == loanId })
					.collect(Collectors.toList());
	}
	
	@Override
	public void setActiveWithLoanId(Long loanId) {
		List<Investment> investmentsList = findWithLoanId(loanId);
		investmentsList.forEach({ investment ->
			investment.setStatus(Investment.Status.ACTIVE);
			repo.put(investment.getInvestmentId(), investment);
		});
	}
	
	@Override
	public void deleteAll() {
		repo.clear();
	}
}

