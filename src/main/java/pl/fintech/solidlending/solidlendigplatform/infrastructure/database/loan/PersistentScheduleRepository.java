package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentSchedule;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentScheduleRepository;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PersistentScheduleRepository implements RepaymentScheduleRepository {
	JpaScheduleRepository jpaScheduleRepository;
	JpaLoanRepository jpaLoanRepository;
	JpaInvestmentRepository jpaInvestmentRepository;
	@Override
	public Long save(RepaymentSchedule schedule) {
		return jpaScheduleRepository.save(RepaymentScheduleEntity.from(schedule)).getId();
	}

	@Override
	public void update(Long id, RepaymentSchedule schedule) {
		schedule.setId(id);
		save(schedule);
	}
	
	@Override
	public void deleteAll(){
		jpaScheduleRepository.deleteAll();
	}
}
