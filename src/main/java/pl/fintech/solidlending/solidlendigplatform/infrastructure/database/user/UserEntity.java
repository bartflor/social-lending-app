package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Lender;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.User;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String userName;
	private String name;
	private String surname;
	private String email;
	private String phoneNumber;
	private UUID platformAccountNumber;
	private UUID privateAccountNumber;
	@Enumerated(EnumType.STRING)
	private Role role;
	private Double ratingValue;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private List<OpinionEntity> borrowerOpinions;
	
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
			Rating rating = new Rating( ratingValue,
					borrowerOpinions.stream()
							.map(OpinionEntity::toDomain)
							.collect(Collectors.toList()));
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
        .ratingValue(user instanceof Borrower ? ((Borrower) user).getRating().getTotalRating() : 0)
		.borrowerOpinions(user instanceof Borrower ?
				((Borrower) user).getRating().getOpinions().stream()
						.map(OpinionEntity::from)
						.collect(Collectors.toList())
				: Collections.emptyList())
        .userName(userName)
        .name(details.getName())
        .surname(details.getSurname())
        .phoneNumber(details.getPhoneNumber())
        .build();
	}
}
