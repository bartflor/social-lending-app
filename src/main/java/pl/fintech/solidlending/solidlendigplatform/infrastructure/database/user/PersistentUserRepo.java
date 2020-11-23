package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Lender;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.LenderRepository;

import java.util.Optional;
@Component
@AllArgsConstructor
public class PersistentUserRepo implements BorrowerRepository, LenderRepository {
	JpaUserRepository jpaUserRepository;
	
	@Override
	public Optional<Borrower> findBorrowerByUserName(String userName) {
		return jpaUserRepository.findByUserNameAndRole(userName, UserEntity.Role.BORROWER)
				.map(user -> (Borrower)user.createDomainUser());
	}
	
	@Override
	public boolean borrowerExists(String userName) {
		return jpaUserRepository.findByUserNameAndRole(userName, UserEntity.Role.BORROWER)
				.isPresent();
		
	}
	
	@Override
	public String save(Borrower borrower) {
		jpaUserRepository.save(UserEntity.createEntityFrom(borrower));
		return borrower.getUserDetails().getUserName();
	}
	
	@Override
	public Optional<Lender> findLenderByUserName(String userName) {
		return jpaUserRepository.findByUserNameAndRole(userName, UserEntity.Role.LENDER)
				.map(user -> (Lender)user.createDomainUser());
	}
	
	@Override
	public boolean lenderExist(String lenderName) {
		return jpaUserRepository.findByUserNameAndRole(lenderName, UserEntity.Role.LENDER)
				.isPresent();
	}
	
	@Override
	public String save(Lender lender) {
		jpaUserRepository.save(UserEntity.createEntityFrom(lender));
		return lender.getUserDetails().getUserName();
	}
	
	@Override
	public void deleteAll(){
		jpaUserRepository.deleteAll();
	}
}
