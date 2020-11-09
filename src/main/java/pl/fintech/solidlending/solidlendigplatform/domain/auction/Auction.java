package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
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
	@Builder.Default private AuctionStatus status = AuctionStatus.ACTIVE;
	private final AuctionLoanParams auctionLoanParams;
	
	public void addNewOffer(Offer offer) {
		offers.add(offer);
		Money offersSum = offers.stream()
				.map(Offer::getAmount)
				.reduce(Money::sum)
				.orElse(Money.ZERO);
		if(offersSum.isMoreOrEqual(auctionLoanParams.getLoanAmount())){
			updateStatus(AuctionStatus.ACTIVE_COMPLETE);
		}
	}
	
	public void updateStatus(AuctionStatus status) {
		this.status = status;
	}
	
	public enum AuctionStatus {
		ACTIVE, ARCHIVED, SUSPENDED, ACTIVE_COMPLETE
	}
	
}
