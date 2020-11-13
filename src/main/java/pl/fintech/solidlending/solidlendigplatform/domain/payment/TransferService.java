package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

public interface TransferService {
	String makeInternalTransfer(String sourceUserName, String targetUserName, Money amount);
}
