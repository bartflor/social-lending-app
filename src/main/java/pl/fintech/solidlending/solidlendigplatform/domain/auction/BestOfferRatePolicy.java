package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * First implementation assumes that only one offer with lowest rate will be selected (one loan -> one investment)
 */
@EqualsAndHashCode
public class BestOfferRatePolicy implements OffersSelectionPolicy {
	@Override
	public Set<Offer> selectOffers(Set<Offer> offers, AuctionLoanParams auctionLoanParams) {
		Comparator comparator = new Offer.OfferRateComparator();
		Offer bestOffer = offers.stream()
					.min(comparator::compare)
					.get();
			return Collections.singleton(bestOffer);
	}
}

