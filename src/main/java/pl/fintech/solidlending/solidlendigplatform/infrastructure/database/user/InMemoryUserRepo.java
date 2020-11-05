package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.user.BorrowerRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.user.LenderRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.user.User;


import java.math.BigDecimal;
import java.util.*;
@Repository
public class InMemoryUserRepo implements BorrowerRepository, LenderRepository {
	Map<Integer, UserEntity> repository;
	
	public InMemoryUserRepo() {
		this.repository  = new HashMap<>();
		repository.put(1,new UserEntity("testBorrower", "borrower@mail", UUID.randomUUID().toString(), BigDecimal.ZERO, UserEntity.Role.BORROWER, 3));
		repository.put(2, new UserEntity("testLender", "lender@mail", UUID.randomUUID().toString(), BigDecimal.TEN, UserEntity.Role.LENDER, 0));
	}
	
	@Override
	public Optional<Borrower> findByUserName(String userName) {
		if(repository.containsKey(userName)){
			User user = UserEntity.createDomainUser(repository.get(userName));
			if(user instanceof Borrower)
			return Optional.of((Borrower)user);
		}
		return Optional.empty();
	}
	
	@Override
	public void addBorrowerAuction(String auctionOwnerId, Long auctionId) {
	
	}
}
