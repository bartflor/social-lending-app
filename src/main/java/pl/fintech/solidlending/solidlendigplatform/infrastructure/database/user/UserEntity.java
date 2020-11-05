package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Lender;
import pl.fintech.solidlending.solidlendigplatform.domain.user.User;
import pl.fintech.solidlending.solidlendigplatform.domain.user.UserDetails;

import java.math.BigDecimal;
@Builder
@AllArgsConstructor
@ToString
public class UserEntity {
	private String userName;
	private String email;
	private String accountNumber;
	private BigDecimal balanceValue;
	private Role role;
	private int ratingValue;
	
	protected enum Role{
		LENDER, BORROWER
	}
	public User createDomainUser(){
		UserDetails details = new UserDetails(userName, email, accountNumber);
		Money balance = new Money(balanceValue);
		if(role == Role.LENDER){
			return Lender.builder()
					.balance(balance)
					.userDetails(details)
					.build();
		}
		else {
			Rating rating = new Rating(ratingValue);
			return Borrower.builder()
					.userDetails(details)
					.balance(balance)
					.rating(rating)
					.build();
					
		}
	}
}
