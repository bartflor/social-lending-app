package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.Data;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Data
public class RepaymentSchedule {
	private static final String NO_REPAYMENT_TO_REPORT_TRANSFER = "Repayment for transaction report not found.";
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
	
	public Optional<Repayment> findNextRepayment() {
		return schedule.stream()
				.filter(repayment -> !repayment.getStatus().equals(Repayment.Status.PAID))
				.min(Comparator.comparing(Repayment::getDate));
	}
	
	public boolean hasLateRepayment(){
		return schedule.stream()
				.filter(repayment -> repayment.getStatus().equals(Repayment.Status.LATE))
				.count() != 0;
	}

	public boolean hasPaidAllScheduledRepayment() {
		return schedule.stream()
				.filter(repayment -> !repayment.getStatus().equals(Repayment.Status.PAID))
				.count() == 0;
	}
	
	public void addRepayment(Repayment repayment) {
		schedule.add(repayment);
	}
	
	public void reportRepayment() {
		Repayment repayment = findNextRepayment()
				.orElseThrow(() -> new NoSuchElementException(NO_REPAYMENT_TO_REPORT_TRANSFER));
		repayment.isPaid();
	}
	
}
