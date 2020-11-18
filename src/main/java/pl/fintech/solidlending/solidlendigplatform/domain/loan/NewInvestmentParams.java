package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;

import java.time.Instant;
import java.time.Period;
@Builder
@Value
public class NewInvestmentParams {
	String LenderUserName;
	String BorrowerName;
	Money investedMoney;
	Risk risk;
	Rate returnRate;
	Period investmentDuration;
	Instant investmentStartDate;
}