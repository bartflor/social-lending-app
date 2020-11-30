package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.ExternalTransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.TransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.UserService;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.User;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.payment.exception.TransferFailException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class PaymentApplicationServiceImpl implements PaymentApplicationService {
	private static final String USERS_HAVE_NO_ACCOUNT = "User with provided user names: %s, %s has no platform bank account number specified";
	private static final String USER_HAVE_NO_LINKED_ACCOUNT = "User: %s has no linked bank account";
	private BankClient bankClient;
	private UserService userService;
	
	
	@Override
	public String execute(TransferOrderEvent transferOrderEvent){
		User sourceUser = userService.findUser(transferOrderEvent.getSourceUserName());
		User targetUser = userService.findUser(transferOrderEvent.getTargetUserName());
		BigDecimal amount = transferOrderEvent.getAmount().getValue();
		if(sourceUser.getPlatformBankAccount() == null || targetUser.getPlatformBankAccount() == null){
			throw new TransferFailException(String.format(USERS_HAVE_NO_ACCOUNT,
					transferOrderEvent.getSourceUserName(), transferOrderEvent.getTargetUserName()));
		}
		return bankClient.transfer(sourceUser.getPlatformBankAccount(),
				targetUser.getPlatformBankAccount(),
				amount.doubleValue());
	}
	
	@Override
	public void execute(List<TransferOrderEvent> transferOrderEventsList) {
		transferOrderEventsList.forEach(this::execute);
	}
	
	@Override
	public Money checkUserBalance(String userName){
		UUID accountNumber = userService.findUser(userName).getPlatformBankAccount();
		return new Money(bankClient.getAccountBalance(accountNumber));
	}
	
	@Override
	public void executeExternal(ExternalTransferOrderEvent transferOrderEvent){
		User user = userService.findUser(transferOrderEvent.getUserName());
		if (!user.hasLinkedBankAccount()) {
			throw new TransferFailException(String.format(USER_HAVE_NO_LINKED_ACCOUNT, transferOrderEvent.getUserName()));
		}
		if(transferOrderEvent.isDepositTransfer()){
			bankClient.transfer(user.getPrivateBankAccount(),
					user.getPlatformBankAccount(),
					transferOrderEvent.getAmount().getValue().doubleValue());
		} else {
			bankClient.transfer(user.getPlatformBankAccount(),
					user.getPrivateBankAccount(),
					transferOrderEvent.getAmount().getValue().doubleValue());
		}
	}
	
	@Override
	public boolean hasEnoughFundsToPay(String userName, Money amount){
		return checkUserBalance(userName).isMoreOrEqual(amount);
	}

}
