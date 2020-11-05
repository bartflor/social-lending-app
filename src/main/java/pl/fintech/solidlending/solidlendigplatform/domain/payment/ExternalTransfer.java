package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import pl.fintech.solidlending.solidlendigplatform.domain.user.User;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

class ExternalTransfer<T extends User> {
	private T user;
	private Money amount;
	private TransferType paymentType;
	
	public enum TransferType{
		WITHDRAW, PAYMENT
	}
	
}
