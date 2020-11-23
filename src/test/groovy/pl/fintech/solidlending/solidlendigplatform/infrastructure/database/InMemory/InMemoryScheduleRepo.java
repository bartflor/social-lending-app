package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory;

import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentSchedule;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentScheduleRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

	@Override
	public void update(Long id, RepaymentSchedule schedule) {
		repo.put(id, schedule);
	}
	
	private Optional<RepaymentSchedule> getScheduleOfTypeById(Long loanId, RepaymentSchedule.Type investment) {
		return repo.values().stream()
				.filter(schedule -> schedule.getType().equals(investment))
				.filter(schedule -> schedule.getOwnerId().equals(loanId))
				.findAny();
	}
}
