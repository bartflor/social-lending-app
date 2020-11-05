package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Lender;
import pl.fintech.solidlending.solidlendigplatform.domain.user.User;
import pl.fintech.solidlending.solidlendigplatform.domain.user.UserDetails;

import java.math.BigDecimal;
@Builder
@AllArgsConstructor
public class UserEntity {
	private String userName;
	private String email;
	private String accountNumber;
	private BigDecimal balance;
	private Role role;
	private int rating;
	
	protected enum Role{
		LENDER, BORROWER
	}
	public static User createDomainUser(UserEntity dbUser){
		UserDetails details = new UserDetails(dbUser.userName, dbUser.email, dbUser.accountNumber);
		Money balance = new Money(dbUser.balance);
		if(dbUser.role == Role.LENDER){
			return Lender.builder()
					.balance(balance)
					.userDetails(details)
					.build();
		}
		else {
			return Borrower.builder()
					.userDetails(details)
					.balance(balance)
					.build();
					
		}
	}
}
