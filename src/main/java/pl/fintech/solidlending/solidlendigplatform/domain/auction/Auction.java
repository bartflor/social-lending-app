package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.EndAuctionEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanCreationException;

import java.time.Instant;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@Getter
public class Auction {
	private static final String INCORRECT_AUCTION_STATUS = "Can not create loan from auction with status: %s";
	
	@Setter
	private Long id;
	@Setter
	private Borrower borrower;
	private final Instant endDate;
	private final Period auctionDuration;
	@Builder.Default private Set<Offer> offers = new HashSet<>();
	@Builder.Default private AuctionStatus status = AuctionStatus.ACTIVE;
	private final Money loanAmount;
	private final Period loanDuration;
	private final Rate loanRate;
	
	public enum AuctionStatus {
		ACTIVE, ARCHIVED, ACTIVE_COMPLETE
	}
	
	void addNewOffer(Offer offer) {
		offers.add(offer);
		checkStatusUpdate();
	}
	
	void removeOffer(Offer offer) {
		offers.remove(offer);
		checkStatusUpdate();
	}
	
	void checkStatusUpdate() {
		Money loanAmount = getLoanAmount();
		Money offersSum = offers.stream()
				.map(Offer::getAmount)
				.reduce(Money::sum)
				.orElse(Money.ZERO);
		if(offersSum.isMoreOrEqual(loanAmount)){
			status = AuctionStatus.ACTIVE_COMPLETE;
    	} else {
      		status = AuctionStatus.ACTIVE;
		}
	}
	
	EndAuctionEvent end(OffersSelectionPolicy selectionPolicy) {
		if(!status.equals(Auction.AuctionStatus.ACTIVE_COMPLETE))
			throw new LoanCreationException(String.format(INCORRECT_AUCTION_STATUS, status));
		Set<Offer> bestOffers = selectionPolicy.selectOffers(offers, loanAmount);
		status = AuctionStatus.ARCHIVED;
		return EndAuctionEvent.builder()
				.BorrowerUserName(borrower.getUserDetails().getUserName())
				.offerParamsSet((bestOffers.stream()
						.map(Offer::toOfferParams)
						.collect(Collectors.toSet())))
				.loanAmount(loanAmount)
				.loanDuration(loanDuration)
				.build();
	}
	

}
