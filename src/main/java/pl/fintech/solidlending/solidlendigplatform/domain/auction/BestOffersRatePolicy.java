package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.EqualsAndHashCode;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.OfferNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Looking for Offers with lowest rate, and adding them to set of best offers. If added offer makes total offers sum
 * bigger than loan amount, it is reduced to meet requested value.
 */
@EqualsAndHashCode
public class BestOffersRatePolicy implements OffersSelectionPolicy {
	private static final String NO_OFFERS_IN_AUCTION = "Can not select offer from auction - no offers provided!";
	
	@Override
	public Set<Offer> selectOffers(Set<Offer> offers, AuctionLoanParams auctionLoanParams) {
		Set<Offer> auctionOffers = offers.stream().map(Offer::new)
				.collect(Collectors.toSet());
		Set<Offer> bestOffersSet = new HashSet<>();
		Comparator<Offer> comparator = new Offer.OfferAmountRateComparator();
		Money loanAmount = auctionLoanParams.getLoanAmount();
		while(!auctionOffers.isEmpty()){
			Money bestOffersSum = bestOffersSet.stream()
				.map(Offer::getAmount)
				.reduce(Money::sum)
				.orElse(Money.ZERO);
			if(bestOffersSum.isEqual(loanAmount)){
				return bestOffersSet;
			}
			Offer bestOffer = auctionOffers.stream()
						.min(comparator)
						.orElseThrow(() -> new OfferNotFoundException(NO_OFFERS_IN_AUCTION));
			auctionOffers.remove(bestOffer);
			
			Money neededDifference = Money.differenceValue(loanAmount,bestOffersSum);
			if (!neededDifference.isMoreOrEqual(bestOffer.getAmount())) {
				bestOffer.reduceTo(neededDifference);
			}
			bestOffersSet.add(bestOffer);
		}
		return bestOffersSet;
	}
}

