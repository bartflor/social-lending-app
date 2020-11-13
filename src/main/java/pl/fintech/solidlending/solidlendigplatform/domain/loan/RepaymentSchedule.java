package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.Data;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.time.LocalDate;
import java.util.*;

@Data
public class RepaymentSchedule {
	Long loanId;
	List<Repayment> schedule;
	
	public RepaymentSchedule() {
		schedule = new ArrayList<>();
	}
	
	public void addRepayment(LocalDate date, Money value) {
		schedule.add(Repayment.builder().date(date).value( value).build());
	}
	
	public Optional<Repayment> findNextRepayment() {
		return schedule.stream()
				.filter(repayment -> !repayment.getStatus().equals(Repayment.Status.PAID))
				.min(Comparator.comparing(Repayment::getDate));
	}
	
	boolean hasLateRepayment(){
		return schedule.stream()
				.filter(repayment -> repayment.getStatus().equals(Repayment.Status.LATE))
				.count() != 0;
	}
	
	boolean hasPaidAllScheduledRepayment(){
		return schedule.stream()
				.filter(repayment -> !repayment.getStatus().equals(Repayment.Status.PAID))
				.count() == 0;
	}
}
