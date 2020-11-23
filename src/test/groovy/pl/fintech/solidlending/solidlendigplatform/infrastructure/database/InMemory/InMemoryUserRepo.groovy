package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory


import pl.fintech.solidlending.solidlendigplatform.domain.common.user.*
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user.UserEntity

class InMemoryUserRepo implements BorrowerRepository, LenderRepository {
	Map<String, UserEntity> repository
	
	InMemoryUserRepo() {
		this.repository  = new HashMap<>()
	}
	
	@Override
	Optional<Borrower> findBorrowerByUserName(String userName) {
		if(repository.containsKey(userName)){
			User user = (repository.get(userName)).createDomainUser()
			if(user instanceof Borrower)
			return Optional.of((Borrower)user)
		}
		return Optional.empty()
	}
	
	@Override
	boolean borrowerExists(String userName) {
		return findBorrowerByUserName(userName).isPresent()
	}
	
	@Override
	String save(Borrower borrower) {
		String userName = borrower.getUserDetails().getUserName()
		repository.put(userName, UserEntity.createEntityFrom(borrower))
		return userName
	}

	@Override
	User updateBorrowerDetails(Borrower user, Map<String, String> newDetails) {
		return null
	}

	@Override
	void deleteAll() {
		repository.clear()
	}
	
	String save(Lender lender) {
		String userName = lender.getUserDetails().getUserName()
		repository.put(userName, UserEntity.createEntityFrom(lender))
		return userName
	}

	@Override
	User updateLenderDetails(Lender user, Map<String, String> newDetails) {
		return null
	}

	@Override
	Optional<Lender> findLenderByUserName(String userName) {
		if(repository.containsKey(userName)){
			User user = repository.get(userName).createDomainUser()
			if(user instanceof Lender)
				return Optional.of((Lender)user)
		}
		return Optional.empty()
	}
	
	@Override
	boolean lenderExist(String lenderName) {
		return findLenderByUserName(lenderName).isPresent()
	}
}
