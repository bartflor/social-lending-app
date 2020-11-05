package pl.fintech.solidlending.solidlendigplatform.domain.user;

import java.util.Optional;

public interface BorrowerRepository {
	Optional<Borrower> findBorrowerByUserName(String userName);
	
	void addBorrowerAuction(String auctionOwnerId, Long auctionId);
	
	boolean borrowerExists(String userName);
}
