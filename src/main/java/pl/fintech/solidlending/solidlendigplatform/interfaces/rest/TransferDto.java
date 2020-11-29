package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.Data;
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.ExternalTransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

@Data
public class TransferDto {
	Double amount;
	String userName;
	
	public ExternalTransferOrderEvent createTransferOrderEvent(ExternalTransferOrderEvent.TransferType transferType) {
		return ExternalTransferOrderEvent.builder()
				.amount(new Money(amount))
				.userName(userName)
				.transferType(transferType)
				.build();
	}

}
