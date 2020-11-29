package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.BestOfferParams;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

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
  private final String lenderName;
  private Money amount;
  private final Rate rate;
  private final Period duration;
  @Builder.Default private OfferStatus status = OfferStatus.ACTIVE;
  
  public Offer(Offer offer){
    this.id = offer.getId();
    this.auctionId = offer.getAuctionId();
    this.lenderName = offer.getLenderName();
    this.amount = new Money(offer.getAmount()!=null ? offer.getAmount().getValue().doubleValue() : 0.0);
    this.rate = Rate.fromPercentValue(offer.getRate()!=null ? offer.getRate().getPercentValue().doubleValue() : 0);
    this.duration = offer.getDuration();
    this.status = offer.getStatus();
  }

  
  public void archive() {
    status = OfferStatus.ARCHIVED;
  }
  
  public BigDecimal getRatePercentValue(){
    return rate.getPercentValue();
  }
  
  public enum OfferStatus{
    ACTIVE, ARCHIVED
  }
  
  public void reduceTo(Money reducedAmount){
    if(amount.isMoreOrEqual(reducedAmount)){
      amount = reducedAmount;
    }
  }
  
  public static class OfferAmountRateComparator implements Comparator<Offer> {
    @Override
    public int compare(Offer offer1, Offer offer2) {
      int rateComparison = offer1.getRatePercentValue().compareTo(offer2.getRatePercentValue());
      int amountComparison = offer1.getAmount().getValue().compareTo(offer2.getAmount().getValue());
      int idComparison = offer1.getId().compareTo(offer2.getId());
  
      if(rateComparison != 0){
        return rateComparison;
      }
      if(amountComparison != 0){
        return amountComparison;
      }
      return idComparison;
    }
  }
  
  public BestOfferParams toOfferParams(){
    return BestOfferParams.builder()
            .amount(getAmount())
            .lenderName(getLenderName())
            .rate(getRate())
            .build();
  }
}