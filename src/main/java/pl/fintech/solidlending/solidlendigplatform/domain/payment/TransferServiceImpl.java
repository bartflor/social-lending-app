package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.TransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.payment.exception.TransferFailException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TransferServiceImpl implements TransferService {
	private static final String USER_NOT_FOUND = "User with username:%s not found.";
	private static final String USERS_HAVE_NO_ACCOUNT = "User with provided user names: %s, %s has no bank account number specified";
	private BankClient bankClient;
	private LenderRepository lenderRepository;
	private BorrowerRepository borrowerRepository;
	
	
	@Override
	public String execute(TransferOrderEvent transferOrderEvent){
		User sourceUser = findUser(transferOrderEvent.getSourceUserName());
		User targetUser = findUser(transferOrderEvent.getTargetUserName());
		BigDecimal amount = transferOrderEvent.getAmount().getValue();
		if(sourceUser.hasBankAccount() && targetUser.hasBankAccount()){
			return bankClient.transfer(sourceUser.getBankAccount(),
					targetUser.getBankAccount(),
					amount.doubleValue());
		} else {
			throw new TransferFailException(String.format(USERS_HAVE_NO_ACCOUNT, sourceUser, targetUser));
		}
	}
	
	@Override
	public void execute(List<TransferOrderEvent> transferOrderEventsList) {
		transferOrderEventsList.forEach(this::execute);
	}
	
	private User findUser(String userName){
		Optional<Lender> lender = lenderRepository.findLenderByUserName(userName);
		Optional<Borrower> borrower = borrowerRepository.findBorrowerByUserName(userName);
		return borrower.isPresent() ? borrower.get() : lender.orElseThrow(()
				-> new UserNotFoundException(String.format(USER_NOT_FOUND, userName)));
		}
	
}
