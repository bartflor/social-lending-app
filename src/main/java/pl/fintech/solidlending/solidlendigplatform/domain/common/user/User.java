package pl.fintech.solidlending.solidlendigplatform.domain.common.user;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
public abstract class User {
	private UserDetails userDetails;
	
	public boolean hasLinkedBankAccount(){
		return userDetails.getPrivateAccountNumber() != null;
	}
	
	public UUID getPlatformBankAccount(){
		return userDetails.getPlatformAccountNumber();
	}
	
	public UUID getPrivateBankAccount(){
		return userDetails.getPrivateAccountNumber();
	}
}
