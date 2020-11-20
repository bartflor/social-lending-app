package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;

import java.math.BigDecimal;
import java.time.Period;
import java.util.Comparator;
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Builder
public class Offer {
  @Setter private Long id;
  @Setter private Long auctionId;
  private String lenderName;
  private String borrowerName;
  private Money amount;
  private Risk risk;
  private Rate rate;
  private Period duration;
  private Boolean allowAmountSplit;
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