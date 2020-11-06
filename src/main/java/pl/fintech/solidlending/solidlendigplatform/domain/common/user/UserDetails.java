package pl.fintech.solidlending.solidlendigplatform.domain.common.user;

import lombok.Value;

@Value
public class UserDetails {
	String userName;
	String email;
	String accountNumber;
}
