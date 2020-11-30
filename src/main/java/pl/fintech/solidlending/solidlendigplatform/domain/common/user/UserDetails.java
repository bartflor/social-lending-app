package pl.fintech.solidlending.solidlendigplatform.domain.common.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class UserDetails {
	String userName;
	@Setter String name;
	@Setter String surname;
	@Setter String email;
	@Setter String phoneNumber;
	@Setter String address;
	UUID platformAccountNumber;
	@Setter UUID privateAccountNumber;
}
