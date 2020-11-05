package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.Getter;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Lender;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

import java.time.Period;
@Getter
public class Offer {
	Lender owner;
	Money amount;
	Rate rate;
	Period duration;
}
