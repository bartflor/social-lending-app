package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDetailsDto {
	String userName;
	String name;
	String surname;
	String email;
	String phoneNumber;
	BigDecimal accountBalance;
	Boolean hasLinkedBankAccount;
	
	public static UserDetailsDto from(UserDetails userDetails, BigDecimal accountBalance){
		return UserDetailsDto.builder()
				.userName(userDetails.getUserName())
				.name(userDetails.getName())
				.surname(userDetails.getSurname())
				.email(userDetails.getEmail())
				.phoneNumber(userDetails.getPhoneNumber())
				.accountBalance(accountBalance)
				.hasLinkedBankAccount(userDetails.getPrivateAccountNumber()!=null)
				.build();
	}
	
	public UserDetails toDomain() {
		return UserDetails.builder()
				.userName(userName)
				.phoneNumber(phoneNumber)
				.email(email)
				.name(name)
				.surname(surname)
				.build();
	}
}
