package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

@Value
public class OfferDto {
    Long offerId;
    Long auctionId;
    String lenderUserName;
    double amount;
    double rate;
    String status;

    public static OfferDto fromOffer(Offer offer) {
        return new OfferDto(
              offer.getId(),
              offer.getAuctionId(),
              offer.getLenderName(),
              offer.getAmount().getValue().doubleValue(),
              offer.getRate().getPercentValue().doubleValue(),
                offer.getStatus().toString());
    }
    
    public Offer createDomainOffer(){
        return Offer.builder()
                .auctionId(auctionId)
                .id(offerId)
                .lenderName(lenderUserName)
                .amount(new Money(amount))
                .rate(Rate.fromPercentDouble(rate))
                .build();
    }
  
}
