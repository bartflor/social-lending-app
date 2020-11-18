package pl.fintech.solidlending.solidlendigplatform.domain.common.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Value
public class UserDetails {
	String userName;
	String name;
	String surname;
	String email;
	String phoneNumber;
	UUID platformAccountNumber;
	UUID privateAccountNumber;
}
