package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import java.util.Set;

interface OffersSelectionPolicy {
	Set<Offer> selectOffers(Set<Offer> offers, AuctionLoanParams auctionLoanParams);
}
