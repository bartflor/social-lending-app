package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import lombok.extern.java.Log;
import org.springframework.stereotype.Repository;
import pl.fintech.solidlending.solidlendigplatform.domain.user.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Log
@Repository
public class InMemoryUserRepo implements BorrowerRepository, LenderRepository {
	Map<String, UserEntity> repository;
	
	public InMemoryUserRepo() {
		this.repository  = new HashMap<>();
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
	public String save(Borrower borrower) {
		String userName = borrower.getUserDetails().getUserName();
		repository.put(userName, UserEntity.createEntityFrom(borrower));
		return userName;
	}
	public String save(Lender lender) {
		String userName = lender.getUserDetails().getUserName();
		repository.put(userName, UserEntity.createEntityFrom(lender));
		return userName;
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
