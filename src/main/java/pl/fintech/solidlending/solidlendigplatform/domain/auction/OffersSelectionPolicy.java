package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.util.Set;

public interface OffersSelectionPolicy {
	Set<Offer> selectOffers(Set<Offer> offers, Money loanAmount);
}
