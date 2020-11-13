package pl.fintech.solidlending.solidlendigplatform.domain.common.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor
@Builder
@Value
public class UserDetails {
	String userName;
	String name;
	String email;
	String accountNumber;
}
