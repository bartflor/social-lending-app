package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
@Setter
@Getter
@AllArgsConstructor
public class OfferResponse {
	Long offerId;
	String lenderUserName;
	double amount;
	double rate;
	
	public static OfferResponse fromOffer(Offer offer) {
    return new OfferResponse(offer.getId(),
			offer.getLenderName(),
			offer.getAmount().getValue().doubleValue(),
			offer.getRate().getRate());
	}
}
