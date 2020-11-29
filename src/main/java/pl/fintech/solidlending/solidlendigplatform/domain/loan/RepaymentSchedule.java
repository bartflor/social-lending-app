package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.RepaymentException;

import java.time.Instant;
import java.util.*;
@Builder
@AllArgsConstructor
@Data
public class RepaymentSchedule {
	private static final String NO_REPAYMENT_TO_REPORT_TRANSFER = "Repayment for transaction report not found.";
	private static final String SCHEDULE_REPAID = "No repayment left in schedule. %s with id: %s is repaid";
	
	Long id;
	Long ownerId;
	List<Repayment> schedule;
	Type type;
	
	public enum Type{
		LOAN, INVESTMENT
	}
	
	public RepaymentSchedule() {
		schedule = new ArrayList<>();
	}
	
	public void addRepayment(Instant date, Money value) {
		schedule.add(Repayment.builder().date(date).value( value).build());
	}
	
	public Repayment getNextRepayment() {
		return schedule.stream()
				.filter(repayment -> !repayment.getStatus().equals(Repayment.Status.PAID))
				.min(Comparator.comparing(Repayment::getDate))
				.orElseThrow(() -> new RepaymentException(String.format(SCHEDULE_REPAID, type, ownerId)));
	}

	public boolean hasPaidAllScheduledRepayment() {
		return schedule.stream().noneMatch(repayment -> !repayment.getStatus().equals(Repayment.Status.PAID));
	}
	
	public void addRepayment(Repayment repayment) {
		schedule.add(repayment);
	}
	
	public void reportRepayment() {
		getNextRepayment().isPaid();
	}
	
}
