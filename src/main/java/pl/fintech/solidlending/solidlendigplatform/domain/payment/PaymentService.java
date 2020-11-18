package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import pl.fintech.solidlending.solidlendigplatform.domain.common.DepositOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.TransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.WithdrawalOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.util.List;

public interface PaymentService {
	String execute(TransferOrderEvent transferOrderEvent);
	
	void execute(List<TransferOrderEvent> transferOrderEventsList);
	
	Money checkUserBalance(String userName);
	
	void depositMoneyIntoPlatform(DepositOrderEvent event);
	
	void withdrawMoneyFromPlatform(WithdrawalOrderEvent event);
}
