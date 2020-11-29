package pl.fintech.solidlending.solidlendigplatform.domain.common.events;

import lombok.Builder;
import lombok.Data;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

@Data
@Builder
public class BestOfferParams {
	private Money amount;
	private String lenderName;
	private Rate rate;
}
