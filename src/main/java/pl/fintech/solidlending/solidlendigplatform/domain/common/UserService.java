package pl.fintech.solidlending.solidlendigplatform.domain.common;

import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.User;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;

import java.util.Map;

public interface UserService {
	UserDetails getUserDetails(String userName);
	
	User findUser(String userName);
	
	void partialUpdateUserDetails(String userName, Map<String, String> newDetails);
	
	void giveOpinionOnBorrower(String userName, Opinion opinion);
	
	Borrower findBorrower(String borrowerName);
	
	Boolean lenderExists(String lenderName);
	
	Boolean borrowerExists(String borrower);
}
