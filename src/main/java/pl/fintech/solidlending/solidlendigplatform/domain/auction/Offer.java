package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

import java.math.BigDecimal;
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
  @Builder.Default private OfferStatus status = OfferStatus.ACTIVE;
  
  public void archive() {
    status = OfferStatus.ARCHIVED;
  }
  
  public BigDecimal getRatePercentValue(){
    return rate.getPercentValue();
  }
  
  public enum OfferStatus{
    ACTIVE, ARCHIVED
  }
  
  public static class OfferRateComparator implements Comparator<Offer> {
    @Override
    public int compare(Offer offer1, Offer offer2) {
      return offer1.getRatePercentValue().compareTo(offer2.getRatePercentValue());
    }
  }
}