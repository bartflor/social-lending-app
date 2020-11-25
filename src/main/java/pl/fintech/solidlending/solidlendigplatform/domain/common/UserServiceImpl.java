package pl.fintech.solidlending.solidlendigplatform.domain.common;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanNotFoundException;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
@Transactional
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
	
	@Override
	public void partialUpdateUserDetails(String userName, Map<String, String> newDetails) {
		User user = findUser(userName);
		if(user instanceof Lender){
			lenderRepository.updateLenderDetails((Lender)user, newDetails);
		} else {
			borrowerRepository.updateBorrowerDetails((Borrower)user, newDetails);
		}
	}
	
	@Override
	public void giveOpinionOnBorrower(String userName, Opinion opinion){
		borrowerRepository.updateBorrowerOpinion(findBorrower(userName), opinion);
	}
	
	@Override
	public Borrower findBorrower(String borrowerName) {
		return borrowerRepository.findBorrowerByUserName(borrowerName)
				.orElseThrow(()-> new UserNotFoundException(String.format(USER_NOT_FOUND, borrowerName)));
	}
	
}
