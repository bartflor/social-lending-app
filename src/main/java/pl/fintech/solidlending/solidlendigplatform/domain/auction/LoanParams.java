package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;

import java.time.Period;
import java.util.Date;
@AllArgsConstructor
@Builder
@Getter
public class LoanParams {
	private Money loanAmount;
	private Risk loanRisk;
	private Period loanDuration;
	private Date loanStartDate;
	private Rate loanRate;
	
}