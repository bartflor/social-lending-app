package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory


import pl.fintech.solidlending.solidlendigplatform.domain.loan.Investment
import pl.fintech.solidlending.solidlendigplatform.domain.loan.InvestmentRepository

import java.util.stream.Collectors

class InMemoryInvestmentRepo implements InvestmentRepository {
		private Map<Long, Investment> repo
		private static Long lastId

		static {
			lastId = 0l
		}
	
	InMemoryInvestmentRepo() {
			this.repo = new HashMap<>()
		}
		
		Long save(Investment investment) {
			investment.setInvestmentId(++lastId)
			repo.put(lastId, investment)
			return lastId
		}
		
		@Override
		List<Investment> findAllByUsername(String userName) {
			return repo.values().stream().filter(({ investment -> investment.getLenderName().equals(userName) }))
					.collect(Collectors.toList())
		}
	
	List<Investment> findWithLoanId(Long loanId) {
			return repo.values().stream()
					.filter({ investment -> investment.getLoanId() == loanId })
					.collect(Collectors.toList())
	}
	
	@Override
	void setActiveWithLoanId(Long loanId) {
		List<Investment> investmentsList = findWithLoanId(loanId)
		investmentsList.forEach({ investment ->
			investment.setStatus(Investment.Status.ACTIVE)
			repo.put(investment.getInvestmentId(), investment)
		})
	}
	
	@Override
	void deleteAll() {
		repo.clear()
	}
}

