package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionLoanParams;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.Period;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AuctionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String borrowerName;
	private Double borrowerRating;
	private Instant auctionEndDate;
	private Period auctionDuration;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "auctionId")
	private Set<OfferEntity> offers;
	@Enumerated(EnumType.STRING)
	private Auction.AuctionStatus status;
	private BigDecimal loanAmount;
	private Period loanDuration;
	private Double loanRate;
	
	public Auction toDomain() {
		AuctionLoanParams auctionLoanParams = AuctionLoanParams.builder()
				.loanAmount(new Money(loanAmount))
				.loanDuration(loanDuration)
				.loanRate(Rate.fromPercentValue(loanRate))
				.build();
		return Auction.builder()
				.id(id)
				.endDate(auctionEndDate)
				.auctionDuration(auctionDuration)
				.status(status)
				.offers(offers.stream()
						.map(OfferEntity::toDomain)
						.collect(Collectors.toSet()))
				.auctionLoanParams(auctionLoanParams)
				.build();
	}
	
	public static AuctionEntity from(Auction auction){
    return AuctionEntity.builder()
        .id(auction.getId())
        .borrowerName(auction.getBorrower().getUserDetails().getUserName())
        .borrowerRating(auction.getBorrower().getRating().getTotalRating())
        .auctionEndDate(auction.getEndDate())
        .auctionDuration(auction.getAuctionDuration())
        .offers(
            auction.getOffers().stream()
                .map(OfferEntity::createFromOffer)
                .collect(Collectors.toSet()))
        .status(auction.getStatus())
        .loanAmount(auction.getAuctionLoanParams().getLoanAmount().getValue())
        .loanDuration(auction.getAuctionLoanParams().getLoanDuration())
        .loanRate(auction.getAuctionLoanParams().getLoanRate().getPercentValue().doubleValue())
        .build();
	}
}
