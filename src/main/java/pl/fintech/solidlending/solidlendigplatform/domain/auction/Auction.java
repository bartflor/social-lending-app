package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;

import java.time.Period;
import java.util.Set;
@AllArgsConstructor
@Builder
@Getter
public class Auction {
	String borrowerUserName;
	Rating borrowerRating;
	Period auctionDuration;
	Set<Offer> offers;
	AuctionStatus status;
	LoanParams loanParams;
	
	public enum AuctionStatus {
		ACTIVE, ARCHIVED, SUSPENDED, ACTIVE_COMPLETE
	}
	
}
