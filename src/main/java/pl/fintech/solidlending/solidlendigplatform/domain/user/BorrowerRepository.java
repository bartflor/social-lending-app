package pl.fintech.solidlending.solidlendigplatform.domain.user;

import java.util.Optional;

public interface BorrowerRepository {
	Optional<Borrower> findBorrowerByUserName(String userName);
	boolean borrowerExists(String userName);
}
