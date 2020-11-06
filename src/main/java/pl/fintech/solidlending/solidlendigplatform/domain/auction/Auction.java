package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@Getter
public class Auction {
	@Setter
	private Long id;
	private final String borrowerUserName;
	private final Rating borrowerRating;
	private final LocalDate startDate;
	private final Period auctionDuration;
	@Builder.Default private final Set<Offer> offers = new HashSet<>();
	@Builder.Default private final AuctionStatus status = AuctionStatus.ACTIVE;
	private final LoanParams loanParams;
	
	public void addNewOffer(Offer offer) {
		offers.add(offer);
	}
	
	public enum AuctionStatus {
		ACTIVE, ARCHIVED, SUSPENDED, ACTIVE_COMPLETE
	}
	
}
