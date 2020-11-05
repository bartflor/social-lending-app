package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Period;
import java.util.Date;
import java.util.List;

@Builder
public class AuctionEntity {
	private String borrowerName;
	private Period auctionDuration;
	private List<OfferEntity> offers;
	private String status;
	private BigDecimal loanAmount;
	private int loanRisk;
	private Period loanDuration;
	private Date loanStartDate;
	private double loanRate;
	
}
