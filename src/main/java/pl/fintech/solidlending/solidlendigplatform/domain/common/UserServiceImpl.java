package pl.fintech.solidlending.solidlendigplatform.domain.common;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	private static final String USER_NOT_FOUND = "User with username:%s not found.";
	
	private LenderRepository lenderRepository;
	private BorrowerRepository borrowerRepository;
	
	@Override
	public UserDetails getUserDetails(String userName){
		return findUser(userName).getUserDetails();
	}
	
	@Override
	public User findUser(String userName){
		Optional<Lender> lender = lenderRepository.findLenderByUserName(userName);
		Optional<Borrower> borrower = borrowerRepository.findBorrowerByUserName(userName);
		return borrower.isPresent() ? borrower.get() : lender.orElseThrow(()
				-> new UserNotFoundException(String.format(USER_NOT_FOUND, userName)));
	}
	
}