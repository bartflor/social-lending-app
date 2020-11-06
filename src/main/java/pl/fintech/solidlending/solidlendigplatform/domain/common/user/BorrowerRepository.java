package pl.fintech.solidlending.solidlendigplatform.domain.common.user;

import java.util.Optional;

public interface BorrowerRepository {
	Optional<Borrower> findBorrowerByUserName(String userName);
	boolean borrowerExists(String userName);
	String save(Borrower borrower);
}
