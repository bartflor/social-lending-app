package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;

import java.time.LocalDate;
import java.time.Period;
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@Getter
public class LoanParams {
	private Money loanAmount;
	private Risk loanRisk;
	private Period loanDuration;
	private LocalDate loanStartDate;
	private Rate loanRate;
	
}