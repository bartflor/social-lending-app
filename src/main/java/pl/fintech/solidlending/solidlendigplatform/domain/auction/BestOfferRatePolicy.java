package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.EqualsAndHashCode;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.OfferNotFoundException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * First implementation assumes that only one offer with lowest rate will be selected (one loan -> one investment)
 */
@EqualsAndHashCode
public class BestOfferRatePolicy implements OffersSelectionPolicy {
	private static final String NO_OFFERS_IN_AUCTION = "Can not select offer from auction - no offers provided!";
	
	@Override
	public Set<Offer> selectOffers(Set<Offer> offers, AuctionLoanParams auctionLoanParams) {
		Comparator comparator = new Offer.OfferRateComparator();
		Offer bestOffer = offers.stream()
					.min(comparator::compare)
					.orElseThrow(() -> new OfferNotFoundException(NO_OFFERS_IN_AUCTION));
			return Collections.singleton(bestOffer);
	}
}

