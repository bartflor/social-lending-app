package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.Getter;
import lombok.Setter;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
@Getter
@Setter
public class OfferResponse {
	long offerId;
	String lenderUserName;
	double amount;
	double rate;
	
	public static OfferResponse fromOffer(Offer offer) {
		return null;
	}
}
