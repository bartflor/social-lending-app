package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import lombok.Builder;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Lender;

import java.math.BigDecimal;
import java.time.Period;
@Builder
public class OfferEntity {
	private String lenderName;
	private BigDecimal amount;
	private double rate;
	private Period duration;
	
	public static OfferEntity createFromOffer(Offer offer){
		return OfferEntity.builder()
				.amount(offer.getAmount().getValue())
				.lenderName(offer.getOwner().getUserDetails().getUserName())
				.rate(offer.getRate().getRate())
				.duration(offer.getDuration())
				.build();
	}
}
