package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Builder
public class AuctionEntity {
	private Long id;
	private String borrowerName;
	private Period auctionDuration;
	private List<OfferEntity> offers;
	private String status;
	private BigDecimal loanAmount;
	private int loanRisk;
	private Period loanDuration;
	private LocalDate loanStartDate;
	private double loanRate;
	
}
