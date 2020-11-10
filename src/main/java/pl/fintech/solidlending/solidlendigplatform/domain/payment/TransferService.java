package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.LenderRepository;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.User;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user.InMemoryUserRepo;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.BankClientAdapter;

@Component
@AllArgsConstructor
public class TransferService {
	BankClientAdapter bankClient;
	LenderRepository lenderRepository;
	BorrowerRepository borrowerRepository;
	
	public void  makeInternalTransfer(String sourceUserName, String targetUserName, double amount){
			bankClient.transfer(findUser(sourceUserName).getUserDetails().getAccountNumber(),
					findUser(targetUserName).getUserDetails().getAccountNumber(),
					amount);
	}
	
	public User findUser(String userName){
		if(lenderRepository.lenderExist(userName))
			return lenderRepository.findLenderByUserName(userName).get();
		else
			return borrowerRepository.findBorrowerByUserName(userName).get();
	}
}
