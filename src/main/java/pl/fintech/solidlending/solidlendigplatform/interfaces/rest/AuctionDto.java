package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.LoanParams;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class AuctionDto {
    Long id;
    String borrower;
    double borrowerRating;
    double amount;
    double rate;
    LocalDate loanStartDate;
    int loanDuration;//in months
    LocalDate auctionStartDate;
    int auctionDuration;//in days
    String status;
    List<OfferDto> offers;
  
    public static AuctionDto fromAuction(Auction auction) {
        LoanParams params = auction.getLoanParams();
        return new AuctionDto(
            auction.getId(),
            auction.getBorrowerUserName(),
            auction.getBorrowerRating().getRating(),
            params.getLoanAmount().getValue().doubleValue(),
            params.getLoanRate().getRate(),
            params.getLoanStartDate(),
            params.getLoanDuration().getMonths(),
            auction.getStartDate(),
            auction.getAuctionDuration().getDays(),
            auction.getStatus().toString(),
            auction.getOffers().stream().map(OfferDto::fromOffer).collect(Collectors.toList()));
    }
}
