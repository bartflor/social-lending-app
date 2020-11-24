package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException;


import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
		return updateDetails(user.getUserDetails().getUserName(), newDetails);
	}
	
	@Override
	public User updateBorrowerDetails(Borrower user, Map<String, String> newDetails) {
		return updateDetails(user.getUserDetails().getUserName(), newDetails);
	}
	
	private User updateDetails(String userName, Map<String, String> newDetails) {
		UserEntity borrower = jpaUserRepository.findByUserNameAndRole(
				userName, UserEntity.Role.BORROWER)
				.orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userName)));
		for(Map.Entry<String, String> entry: newDetails.entrySet()){
			String e = entry.getKey().toLowerCase();
			switch(e){
				case "name":
					borrower.setName(entry.getValue());
					break;
				case "surname":
					borrower.setSurname(entry.getValue());
					break;
				case "phonenumber":
					borrower.setPhoneNumber(entry.getValue());
					break;
				case "email":
					borrower.setEmail(entry.getValue());
					break;
				case "privateaccountnumber":
          			borrower.setPrivateAccountNumber(UUID.fromString(entry.getValue()));
					break;
			}
		
		}
		jpaUserRepository.save(borrower);
		return borrower.createDomainUser();
	}
	
	@Override
	public void deleteAll(){
		jpaUserRepository.deleteAll();
	}
	
}
