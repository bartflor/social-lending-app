package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import java.util.Optional;

public interface RepaymentScheduleRepository {
	Long save(RepaymentSchedule schedule);
	
	void update(Long id, RepaymentSchedule schedule);
	
	void deleteAll();
}
