package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
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
        return new AuctionDto(
            auction.getId(),
            auction.getBorrowerUserName(),
            auction.getBorrowerRating().getRating(),
            params.getLoanAmount().getValue().doubleValue(),
            params.getLoanRate().getPercentValue().doubleValue(),
            params.getLoanDuration().getMonths(),
            auction.getStartDate(),
            auction.getAuctionDuration().getDays(),
            auction.getStatus().toString(),
            auction.getOffers().stream()
                    .map(OfferDto::fromOffer)
                    .collect(Collectors.toList()));
    }
}
