package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import lombok.extern.java.Log;
import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.user.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@Log
@Repository
public class InMemoryUserRepo implements BorrowerRepository, LenderRepository {
	Map<String, UserEntity> repository;
	
	public InMemoryUserRepo() {
		this.repository  = new HashMap<>();
		repository.put("testBorrower",new UserEntity("testBorrower", "borrower@mail", UUID.randomUUID().toString(), BigDecimal.ZERO, UserEntity.Role.BORROWER, 3));
		repository.put("testLender", new UserEntity("testLender", "lender@mail", UUID.randomUUID().toString(), BigDecimal.TEN, UserEntity.Role.LENDER, 0));
		log.info(repository.toString());
	}
	
	@Override
	public Optional<Borrower> findBorrowerByUserName(String userName) {
		if(repository.containsKey(userName)){
			User user = (repository.get(userName)).createDomainUser();
			if(user instanceof Borrower)
			return Optional.of((Borrower)user);
		}
		return Optional.empty();
	}
	
	@Override
	public boolean borrowerExists(String userName) {
		return findBorrowerByUserName(userName).isPresent();
	}
	
	@Override
	public Optional<Lender> findLenderByUserName(String userName) {
		if(repository.containsKey(userName)){
			User user = repository.get(userName).createDomainUser();
			if(user instanceof Lender)
				return Optional.of((Lender)user);
		}
		return Optional.empty();
	}
	
	@Override
	public boolean lenderExist(String lenderName) {
		return findLenderByUserName(lenderName).isPresent();
	}
}
