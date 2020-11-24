package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionLoanParams;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
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
	private Integer borrowerRating;
	private Instant auctionStartDate;
	private Period auctionDuration;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "auctionId")
	private Set<OfferEntity> offers;
	@Enumerated(EnumType.STRING)
	private Auction.AuctionStatus status;
	private BigDecimal loanAmount;
	private Integer loanRisk;
	private Period loanDuration;
	private Double loanRate;
	
	public Auction toDomain() {
		AuctionLoanParams auctionLoanParams = AuctionLoanParams.builder()
				.loanAmount(new Money(loanAmount))
				.loanDuration(loanDuration)
				.loanRate(Rate.fromPercentValue(loanRate))
				.loanRisk(new Risk(loanRisk))
				.build();
		return Auction.builder()
				.id(id)
				.borrowerUserName(borrowerName)
				.borrowerRating(new Rating(borrowerRating))
				.startDate(auctionStartDate)
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
				.borrowerName(auction.getBorrowerUserName())
				.borrowerRating(auction.getBorrowerRating().getRating())
				.auctionStartDate(auction.getStartDate())
				.auctionDuration(auction.getAuctionDuration())
				.offers(auction.getOffers().stream()
						.map(OfferEntity::createFromOffer)
						.collect(Collectors.toSet()))
				.status(auction.getStatus())
				.loanAmount(auction.getAuctionLoanParams()
						.getLoanAmount()
						.getValue())
				.loanRisk(auction.getAuctionLoanParams()
						.getLoanRisk()
						.getRisk())
				.loanDuration(auction.getAuctionLoanParams()
						.getLoanDuration())
				.loanRate(auction.getAuctionLoanParams()
						.getLoanRate()
						.getPercentValue()
						.doubleValue())
				.build();
	}
}
