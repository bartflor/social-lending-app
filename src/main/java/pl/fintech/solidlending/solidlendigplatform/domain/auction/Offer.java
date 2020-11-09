package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

import java.util.Comparator;
@ToString
@AllArgsConstructor
@Getter
@Builder
public class Offer {
  @Setter private Long id;
  @Setter private Long auctionId;
  private String lenderName;
  private Money amount;
  private Rate rate;
  
  public static class OfferRateComparator implements Comparator<Offer> {
    @Override
    public int compare(Offer offer1, Offer offer2) {
      return Double.compare(offer1.getRate().getRateValue(), offer2.getRate().getRateValue());
    }
  }
}