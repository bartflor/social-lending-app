package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentSchedule;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentScheduleRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Repository
public class InMemoryScheduleRepo implements RepaymentScheduleRepository {
	private Map<Long, RepaymentSchedule> repo;
	private static Long lastId;
	
	static {
		lastId = 0l;
	}
	
	public InMemoryScheduleRepo() {
		this.repo = new HashMap<>();
	}
	
	@Override
	public Long save(RepaymentSchedule schedule) {
		repo.put(++lastId, schedule);
		return lastId;
	}
	
	public Optional<RepaymentSchedule> getRepaymentScheduleByLoanId(Long loanId){
		return repo.values().stream()
				.filter(schedule -> schedule.getLoanId().equals(loanId))
				.findAny();
	}
}
