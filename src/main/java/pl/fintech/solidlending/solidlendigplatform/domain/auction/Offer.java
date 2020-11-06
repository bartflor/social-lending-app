package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

import java.time.Period;
@AllArgsConstructor
@Getter
@Builder
public class Offer {
	@Setter
	private Long id;
	@Setter
	private Long auctionId;
	private String lenderName;
	private Money amount;
	private Rate rate;
	private Period duration;
}
