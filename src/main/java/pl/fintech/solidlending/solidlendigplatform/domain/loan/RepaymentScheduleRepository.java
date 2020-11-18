package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import java.util.Optional;

public interface RepaymentScheduleRepository {
	Long save(RepaymentSchedule schedule);
	
	Optional<RepaymentSchedule> findRepaymentScheduleByLoanId(Long loanId);
	
	Optional<RepaymentSchedule> findRepaymentScheduleByInvestmentId(Long loanId);
	
	void update(Long id, RepaymentSchedule schedule);
}
