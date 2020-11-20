package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import pl.fintech.solidlending.solidlendigplatform.domain.common.ExternalTransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.TransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.util.List;

public interface PaymentService {
	String execute(TransferOrderEvent transferOrderEvent);
	
	void execute(List<TransferOrderEvent> transferOrderEventsList);
	
	Money checkUserBalance(String userName);
	
	void executeExternal(ExternalTransferOrderEvent event);
	
	boolean hasEnoughFundsToPay(String userName, Money amount);
}
