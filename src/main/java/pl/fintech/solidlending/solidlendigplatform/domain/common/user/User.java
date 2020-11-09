package pl.fintech.solidlending.solidlendigplatform.domain.common.user;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
@SuperBuilder
@Getter
public abstract class User {
	private UserDetails userDetails;
	private Money balance;
	
	public boolean hasEnoughFundsToPay(Money amount){
		return balance.isMoreOrEqual(amount);
	}
	
	public boolean hasBankAccount(){
		return userDetails.getAccountNumber() != null;
	}
}
