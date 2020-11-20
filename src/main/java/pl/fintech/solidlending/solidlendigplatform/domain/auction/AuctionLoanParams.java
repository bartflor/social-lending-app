package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;

import java.time.Period;
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@Getter
public class AuctionLoanParams {
	private Money loanAmount;
	private Risk loanRisk;
	private Period loanDuration;
	private Rate loanRate;
	
}