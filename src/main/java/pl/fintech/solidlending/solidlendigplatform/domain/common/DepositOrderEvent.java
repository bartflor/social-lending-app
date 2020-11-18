package pl.fintech.solidlending.solidlendigplatform.domain.common;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

@Value
@Builder
public class DepositOrderEvent {
	String userName;
	Money amount;
}
