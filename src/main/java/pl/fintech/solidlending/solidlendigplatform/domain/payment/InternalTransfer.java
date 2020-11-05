package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.user.User;

class InternalTransfer<T extends User, V extends User> {
	T payer;
	V receiver;
	Money amount;
}
