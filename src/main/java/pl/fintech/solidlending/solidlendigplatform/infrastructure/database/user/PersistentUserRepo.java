package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;


import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PersistentUserRepo implements BorrowerRepository, LenderRepository {
	private static final String USER_NOT_FOUND = "User with user name: %s not found";
	JpaUserRepository jpaUserRepository;
	
	@Override
	public Optional<Borrower> findBorrowerByUserName(String userName) {
		return jpaUserRepository.findByUserNameAndRole(userName, UserEntity.Role.BORROWER)
				.map(user -> (Borrower)user.createDomainUser());
	}
	
	@Override
	public boolean borrowerExists(String userName) {
		return jpaUserRepository.findByUserNameAndRole(userName, UserEntity.Role.BORROWER)
				.isPresent();
		
	}
	
	@Override
	public String save(Borrower borrower) {
		jpaUserRepository.save(UserEntity.createEntityFrom(borrower));
		return borrower.getUserDetails().getUserName();
	}
	

	
	@Override
	public Optional<Lender> findLenderByUserName(String userName) {
		return jpaUserRepository.findByUserNameAndRole(userName, UserEntity.Role.LENDER)
				.map(user -> (Lender)user.createDomainUser());
	}
	
	@Override
	public boolean lenderExist(String lenderName) {
		return jpaUserRepository.findByUserNameAndRole(lenderName, UserEntity.Role.LENDER)
				.isPresent();
	}
	
	@Override
	public String save(Lender lender) {
		jpaUserRepository.save(UserEntity.createEntityFrom(lender));
		return lender.getUserDetails().getUserName();
	}
	
	@Override
	public User updateLenderDetails(Lender user, Map<String, String> newDetails) {
    return updateDetails(user.getUserDetails().getUserName(), UserEntity.Role.LENDER, newDetails);
	}
	
	@Override
	public User updateBorrowerDetails(Borrower user, Map<String, String> newDetails) {
		return updateDetails(user.getUserDetails().getUserName(), UserEntity.Role.BORROWER, newDetails);
	}
	
	private User updateDetails(String userName, UserEntity.Role role, Map<String, String> newDetails) {
		UserEntity user = jpaUserRepository.findByUserNameAndRole(userName, role)
				.orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userName)));
		for(Map.Entry<String, String> entry: newDetails.entrySet()){
			String e = entry.getKey().toLowerCase();
			switch(e){
				case "name":
					user.setName(entry.getValue());
					break;
				case "surname":
					user.setSurname(entry.getValue());
					break;
				case "phonenumber":
					user.setPhoneNumber(entry.getValue());
					break;
				case "email":
					user.setEmail(entry.getValue());
					break;
				case "privateaccountnumber":
          			user.setPrivateAccountNumber(UUID.fromString(entry.getValue()));
					break;
			}
		
		}
		jpaUserRepository.save(user);
		return user.createDomainUser();
	}
	
	@Override
	public void deleteAll(){
		jpaUserRepository.deleteAll();
	}
	
	@Override
	public void updateBorrowerOpinion(Borrower borrower) {
		UserEntity borrowerEntity = jpaUserRepository.findByUserNameAndRole(
				borrower.getUserDetails().getUserName(), UserEntity.Role.BORROWER)
				.orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, borrower.getUserDetails().getUserName())));
		borrowerEntity.setBorrowerOpinions(borrower.getRating().getOpinions().stream()
				.map(OpinionEntity::from)
				.collect(Collectors.toList()));
		borrowerEntity.setRatingValue(borrower.getRating().getTotalRating());
		jpaUserRepository.save(borrowerEntity);
		
	}
	
}
