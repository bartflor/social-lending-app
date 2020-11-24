package pl.fintech.solidlending.solidlendigplatform.domain.common;

import pl.fintech.solidlending.solidlendigplatform.domain.common.user.User;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails;

import java.util.Map;

public interface UserService {
	UserDetails getUserDetails(String userName);
	
	User findUser(String userName);
	
	void partialUpdateUserDetails(String userName, Map<String, String> newDetails);
}
