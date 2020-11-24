package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Lender;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.User;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;

import javax.persistence.*;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	private String userName;
	private String name;
	private String surname;
	private String email;
	private String phoneNumber;
	private UUID platformAccountNumber;
	private UUID privateAccountNumber;
	@Enumerated(EnumType.STRING)
	private Role role;
	private int ratingValue;
	
	protected enum Role{
		LENDER, BORROWER
	}
	public User createDomainUser(){
		UserDetails details = UserDetails.builder()
				.userName(userName)
				.name(name)
				.surname(surname)
				.email(email)
				.phoneNumber(phoneNumber)
				.privateAccountNumber(privateAccountNumber)
				.platformAccountNumber(platformAccountNumber)
				.build();
		if(role == Role.LENDER){
			return Lender.builder()
					.userDetails(details)
					.build();
		}
		else {
			Rating rating = new Rating(ratingValue);
			return Borrower.builder()
					.userDetails(details)
					.rating(rating)
					.build();
					
		}
	}
	public static UserEntity createEntityFrom(User user){
		UserDetails details = user.getUserDetails();
		String userName = details.getUserName();
    return UserEntity.builder()
        .privateAccountNumber(details.getPrivateAccountNumber())
        .platformAccountNumber(details.getPlatformAccountNumber())
        .email(details.getEmail())
        .role(user instanceof Borrower ? Role.BORROWER : Role.LENDER)
        .ratingValue(user instanceof Borrower ? ((Borrower) user).getRating().getRating() : 0)
        .userName(userName)
        .name(details.getName())
        .surname(details.getSurname())
        .phoneNumber(details.getPhoneNumber())
        .build();
	}
}
