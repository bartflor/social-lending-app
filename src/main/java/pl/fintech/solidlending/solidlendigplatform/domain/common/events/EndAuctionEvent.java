package pl.fintech.solidlending.solidlendigplatform.domain.common.events;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.time.Period;
import java.util.Set;

@Builder
@Value
public class EndAuctionEvent {
	String BorrowerUserName;
	Set<BestOfferParams> offerParamsSet;
	Money loanAmount;
	Period loanDuration;
	
}
