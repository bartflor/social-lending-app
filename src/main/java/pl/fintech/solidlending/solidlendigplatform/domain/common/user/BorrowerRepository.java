package pl.fintech.solidlending.solidlendigplatform.domain.common.user;

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;

import java.util.Map;
import java.util.Optional;

public interface BorrowerRepository {
	Optional<Borrower> findBorrowerByUserName(String userName);
	boolean borrowerExists(String userName);
	String save(Borrower borrower);
	
	User updateBorrowerDetails(Borrower user, Map<String, String> newDetails);
	
	void deleteAll();
	
	void updateBorrowerOpinion(Borrower borrower, Opinion opinion);
}
