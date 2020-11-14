package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.Data;
import lombok.Setter;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class RepaymentSchedule {
	Long loanId;
	Map<Instant, Repayment> schedule;
	
	public RepaymentSchedule() {
		schedule = new HashMap<>();
	}
	
	public void addRepayment(Instant date, Repayment value) {
		schedule.put(date, value);
	}
}
