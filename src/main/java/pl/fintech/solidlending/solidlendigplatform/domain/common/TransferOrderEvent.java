package pl.fintech.solidlending.solidlendigplatform.domain.common;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
@Value
@Builder
public class TransferOrderEvent {
	String sourceUserName;
	String targetUserName;
	Money amount;
}
