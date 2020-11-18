package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import pl.fintech.solidlending.solidlendigplatform.domain.common.TransferOrderEvent;

import java.util.List;

public interface TransferService {
	String execute(TransferOrderEvent transferOrderEvent);
	
	void execute(List<TransferOrderEvent> transferOrderEventsList);
}
