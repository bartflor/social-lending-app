package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
@AllArgsConstructor
@Builder
@Getter
public class Auction {
	@Setter
	private Long id;
	private String borrowerUserName;
	private Rating borrowerRating;
	private LocalDate startDate;
	private Period auctionDuration;
	private Set<Offer> offers;
	private AuctionStatus status;
	private LoanParams loanParams;
	
	public void addNewOffer(Offer offer) {
		offers.add(offer);
	}
	
	public enum AuctionStatus {
		ACTIVE, ARCHIVED, SUSPENDED, ACTIVE_COMPLETE
	}
	
}
