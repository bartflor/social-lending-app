package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanCreationException;

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
	private static final String INCORRECT_AUCTION_STATUS = "Can not create loan from auction with status: %s";
	
	@Setter
	private Long id;
	private final String borrowerUserName;
	private final Rating borrowerRating;
	private final LocalDate startDate;
	private final Period auctionDuration;
	@Builder.Default private Set<Offer> offers = new HashSet<>();
	@Builder.Default private AuctionStatus status = AuctionStatus.ACTIVE;
	private final AuctionLoanParams auctionLoanParams;
	
	public void addNewOffer(Offer offer) {
		offers.add(offer);
		Money offersSum = offers.stream()
				.map(Offer::getAmount)
				.reduce(Money::sum)
				.orElse(Money.ZERO);
		if(offersSum.isMoreOrEqual(auctionLoanParams.getLoanAmount())){
			status = AuctionStatus.ACTIVE_COMPLETE;
		}
	}
	
	public EndAuctionEvent end(OffersSelectionPolicy selectionPolicy) {
		if(!status.equals(Auction.AuctionStatus.ACTIVE_COMPLETE))
			throw new LoanCreationException(String.format(INCORRECT_AUCTION_STATUS, status));
		Set<Offer> bestOffers = selectionPolicy.selectOffers(offers, auctionLoanParams);
		return EndAuctionEvent.builder()
				.BorrowerUserName(borrowerUserName)
				.offers(bestOffers)
				.auctionLoanParams(auctionLoanParams)
				.build();
	}
	
	public enum AuctionStatus {
		ACTIVE, ARCHIVED, SUSPENDED, ACTIVE_COMPLETE
	}
	
}
