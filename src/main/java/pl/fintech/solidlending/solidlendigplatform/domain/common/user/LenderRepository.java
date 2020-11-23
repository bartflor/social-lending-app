package pl.fintech.solidlending.solidlendigplatform.domain.common.user;

import java.util.Map;
import java.util.Optional;

public interface LenderRepository {
	Optional<Lender> findLenderByUserName(String userName);
	boolean lenderExist(String lenderName);
	
	String save(Lender testLender);
	
	User updateLenderDetails(Lender user, Map<String, String> newDetails);
}
