package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

import javax.persistence.*;
import java.time.Period;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long auctionId;
	private String lenderName;
	private Double amount;
	private Double rate;
	private Period duration;
	@Enumerated(EnumType.STRING)
	private Offer.OfferStatus status;
	
	public static OfferEntity createFromOffer(Offer offer){
    	return OfferEntity.builder()
				.id(offer.getId())
				.auctionId(offer.getAuctionId())
				.lenderName(offer.getLenderName())
				.amount(offer.getAmount().getValue().doubleValue())
				.rate(offer.getRate().getPercentValue().doubleValue())
				.duration(offer.getDuration())
				.status(offer.getStatus())
				.build();
	}
	
	public Offer toDomain() {
		return Offer.builder()
				.id(id)
				.auctionId(auctionId)
				.lenderName(lenderName)
				.amount(new Money(amount))
				.rate(Rate.fromPercentValue(rate))
				.duration(duration)
				.status(status)
				.build();
	}
}
