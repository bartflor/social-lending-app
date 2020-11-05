package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.LoanParams;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Getter
@Setter
@AllArgsConstructor
public class AuctionResponse {
	private long id;
	private String borrower;
	private double borrowerRating;
	private double amount;
	private double rate;
	private LocalDate startDate;
	private int loanDuration;
	private String status;
	private List<OfferResponse> offers;
	
	public static AuctionResponse fromAuction(Auction auction){
		LoanParams params = auction.getLoanParams();
    return new AuctionResponse(
        auction.getId(),
        auction.getBorrowerUserName(),
        2,
        params.getLoanAmount().getValue().doubleValue(),
        params.getLoanRate().getRate(),
        params.getLoanStartDate(),
        params.getLoanDuration().getYears(),
        auction.getStatus().toString(),
        auction.getOffers().stream()
				.map(OfferResponse::fromOffer)
				.collect(Collectors.toList()));
	}
}
