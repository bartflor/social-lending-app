package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import lombok.Builder;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;

import java.math.BigDecimal;
import java.time.Period;
@Builder
public class OfferEntity {
	private String lenderName;
	private BigDecimal amount;
	private double rate;
	
	public static OfferEntity createFromOffer(Offer offer){
		return OfferEntity.builder()
				.amount(offer.getAmount().getValue())
				.lenderName(offer.getLenderName())
				.rate(offer.getRate().getRateValue())
				.build();
	}
}
