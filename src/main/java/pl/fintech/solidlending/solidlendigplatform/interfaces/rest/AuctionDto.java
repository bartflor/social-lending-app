package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionLoanParams;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuctionDto {
    Long id;
    String borrower;
    double borrowerRating;
    double amount;
    double rate;
    int loanDuration;//in months
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Warsaw")
    Instant auctionStartDate;
    int auctionDuration;//in days
    String status;
    List<OfferDto> offers;

    public static AuctionDto fromAuction(Auction auction) {
        AuctionLoanParams params = auction.getAuctionLoanParams();
        return AuctionDto.builder()
                .id(auction.getId())
                .borrower(auction.getBorrowerUserName())
                .borrowerRating(auction.getBorrowerRating().getRating())
                .amount(params.getLoanAmount().getValue().doubleValue())
                .rate(params.getLoanRate().getPercentValue().doubleValue())
                .loanDuration((int)params.getLoanDuration().toTotalMonths())
                .auctionStartDate(auction.getStartDate())
                .auctionDuration(auction.getAuctionDuration().getDays())
                .status(auction.getStatus().toString())
                .offers(auction.getOffers().stream()
                        .map(OfferDto::fromOffer)
                        .collect(Collectors.toList()))
                .build();
    }
}
