package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import pl.fintech.solidlending.solidlendigplatform.domain.common.events.ExternalTransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.TransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.util.List;

public interface PaymentApplicationService {
	String execute(TransferOrderEvent transferOrderEvent);
	
	void execute(List<TransferOrderEvent> transferOrderEventsList);
	
	Money checkUserBalance(String userName);
	
	void executeExternal(ExternalTransferOrderEvent event);
	
	boolean hasEnoughFundsToPay(String userName, Money amount);
}
