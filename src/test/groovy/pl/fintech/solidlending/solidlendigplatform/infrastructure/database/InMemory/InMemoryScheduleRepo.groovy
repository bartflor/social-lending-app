package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory


import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentSchedule
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentScheduleRepository

class InMemoryScheduleRepo implements RepaymentScheduleRepository {
	private Map<Long, RepaymentSchedule> repo
	private static Long lastId
	
	static {
		lastId = 0l
	}
	
	InMemoryScheduleRepo() {
		this.repo = new HashMap<>()
	}
	
	@Override
	Long save(RepaymentSchedule schedule) {
		repo.put(++lastId, schedule)
		return lastId
	}

	@Override
	void update(Long id, RepaymentSchedule schedule) {
		repo.put(id, schedule)
	}
	
	@Override
	void deleteAll() {
		repo.clear()
	}
	
	private Optional<RepaymentSchedule> getScheduleOfTypeById(Long loanId, RepaymentSchedule.Type investment) {
		return repo.values().stream()
				.filter({ schedule -> (schedule.getType() == investment) })
				.filter({ schedule -> (schedule.getOwnerId() == loanId) })
				.findAny()
	}
}
