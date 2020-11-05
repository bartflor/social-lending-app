package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

@Setter
@Getter
@AllArgsConstructor
public class OfferDto {
    Long offerId;
    Long auctionId;
    String lenderUserName;
    double amount;
    double rate;

    public static OfferDto fromOffer(Offer offer) {
        return new OfferDto(
              offer.getId(),
              offer.getAuctionId(),
              offer.getLenderName(),
              offer.getAmount().getValue().doubleValue(),
              offer.getRate().getRate());
    }
    
    public Offer createDomainOffer(){
        return Offer.builder()
                .auctionId(auctionId)
                .id(offerId)
                .lenderName(lenderUserName)
                .amount(new Money(amount))
                .rate(new Rate(rate))
                .build();
    }
  
}
