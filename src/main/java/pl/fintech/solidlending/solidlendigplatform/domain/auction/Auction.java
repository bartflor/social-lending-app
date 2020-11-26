package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.*;
import org.assertj.core.util.BigDecimalComparator;
import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanCreationException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
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
	private final AuctionLoanParams auctionLoanParams;
	
	public void addNewOffer(Offer offer) {
		offers.add(offer);
		if(status != AuctionStatus.ACTIVE_COMPLETE){
			checkStatusUpdate();
		}
	}
	
	private void checkStatusUpdate() {
		status = AuctionStatus.ACTIVE;
		Money loanAmount = auctionLoanParams.getLoanAmount();
		Money offersSum = offers.stream()
				.map(Offer::getAmount)
				.reduce(Money::sum)
				.orElse(Money.ZERO);
		if(offersSum.isMoreOrEqual(loanAmount)){
			status = AuctionStatus.ACTIVE_COMPLETE;
		}
		
	}
	
	public EndAuctionEvent end(OffersSelectionPolicy selectionPolicy) {
		if(!status.equals(Auction.AuctionStatus.ACTIVE_COMPLETE))
			throw new LoanCreationException(String.format(INCORRECT_AUCTION_STATUS, status));
		Set<Offer> bestOffers = selectionPolicy.selectOffers(offers, auctionLoanParams);
		status = AuctionStatus.ARCHIVED;
		return EndAuctionEvent.builder()
				.BorrowerUserName(borrower.getUserDetails().getUserName())
				.offers(bestOffers)
				.auctionLoanParams(auctionLoanParams)
				.build();
	}
	
	public enum AuctionStatus {
		ACTIVE, ARCHIVED, SUSPENDED, ACTIVE_COMPLETE
	}
	
}
