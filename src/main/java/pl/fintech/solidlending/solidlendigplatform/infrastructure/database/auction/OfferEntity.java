package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Period;

import static javax.persistence.FetchType.LAZY;

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
	private String borrowerName;
	private Double amount;
	private Integer risk;
	private Double rate;
	private Period duration;
	@Enumerated(EnumType.STRING)
	private Offer.OfferStatus status;
	
	public static OfferEntity createFromOffer(Offer offer){
    	return OfferEntity.builder()
				.id(offer.getId())
				.auctionId(offer.getAuctionId())
				.lenderName(offer.getLenderName())
				.borrowerName(offer.getBorrowerName())
				.amount(offer.getAmount().getValue().doubleValue())
				.risk(offer.getRisk().getRisk())
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
				.borrowerName(borrowerName)
				.amount(new Money(amount))
				.risk(new Risk(risk))
				.rate(Rate.fromPercentValue(rate))
				.duration(duration)
				.status(status)
				.build();
	}
}
