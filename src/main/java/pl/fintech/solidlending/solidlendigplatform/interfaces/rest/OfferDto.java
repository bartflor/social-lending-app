package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;

import java.time.Period;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OfferDto {
    Long offerId;
    Long auctionId;
    String lenderUserName;
    String borrowerName;
    double amount;
    double rate;
    int risk;
    Period loanDuration;
    String status;
    boolean allowAmountSplit;
    
    public static OfferDto fromOffer(Offer offer) {
        return OfferDto.builder()
                .offerId(offer.getId())
                .auctionId(offer.getAuctionId())
                .lenderUserName(offer.getLenderName())
                .borrowerName(offer.getBorrowerName())
                .amount(offer.getAmount().getValue().doubleValue())
                .rate(offer.getRate().getPercentValue().doubleValue())
                .risk(offer.getRisk().getRisk())
                .loanDuration(offer.getDuration())
                .status(offer.getStatus().toString())
                .allowAmountSplit(offer.getAllowAmountSplit())
                .build();
    }
    
    public Offer createDomainOffer(){
        return Offer.builder()
                .auctionId(auctionId)
                .id(offerId)
                .lenderName(lenderUserName)
                .borrowerName(borrowerName)
                .amount(new Money(amount))
                .risk(new Risk(risk))
                .rate(Rate.fromPercentValue(rate))
                .duration(loanDuration)
                .allowAmountSplit(isAllowAmountSplit())
                .build();
    }
  
}
