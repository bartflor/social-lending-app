package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

import java.time.Period;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OfferDto {
    Long offerId;
    Long auctionId;
    String lenderUserName;
    double amount;
    double rate;
    Period loanDuration;
    String status;
    
    public static OfferDto fromOffer(Offer offer) {
        return OfferDto.builder()
                .offerId(offer.getId())
                .auctionId(offer.getAuctionId())
                .lenderUserName(offer.getLenderName())
                .amount(offer.getAmount().getValue().doubleValue())
                .rate(offer.getRate().getPercentValue().doubleValue())
                .loanDuration(offer.getDuration())
                .status(offer.getStatus().toString())
                .build();
    }
    
    public Offer createDomainOffer(){
        return Offer.builder()
                .auctionId(auctionId)
                .id(offerId)
                .lenderName(lenderUserName)
                .amount(new Money(amount))
                .rate(Rate.fromPercentValue(rate))
                .duration(loanDuration)
                .build();
    }
  
}
