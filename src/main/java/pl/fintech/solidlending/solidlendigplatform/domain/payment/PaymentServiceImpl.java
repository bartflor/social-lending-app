package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.DepositOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.TransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.UserService;
import pl.fintech.solidlending.solidlendigplatform.domain.common.WithdrawalOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.payment.exception.TransferFailException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	private static final String USERS_HAVE_NO_ACCOUNT = "User with provided user names: %s, %s has no platform bank account number specified";
	private static final String USER_HAVE_NO_LINKED_ACCOUNT = "User: %s has no linked bank account";
	private BankClient bankClient;
	private UserService userService;
	
	
	@Override
	public String execute(TransferOrderEvent transferOrderEvent){
		User sourceUser = userService.findUser(transferOrderEvent.getSourceUserName());
		User targetUser = userService.findUser(transferOrderEvent.getTargetUserName());
		BigDecimal amount = transferOrderEvent.getAmount().getValue();
		if(sourceUser.getPlatformBankAccount() != null && targetUser.getPlatformBankAccount() != null){
			return bankClient.transfer(sourceUser.getPlatformBankAccount(),
					targetUser.getPlatformBankAccount(),
					amount.doubleValue());
		} else {
			throw new TransferFailException(String.format(USERS_HAVE_NO_ACCOUNT, sourceUser, targetUser));
		}
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
	public void depositMoneyIntoPlatform(DepositOrderEvent event){
		User user = userService.findUser(event.getUserName());
		if (user.hasLinkedBankAccount()) {
			bankClient.transfer(user.getPrivateBankAccount(),
					user.getPlatformBankAccount(),
					event.getAmount().getValue().doubleValue());
		} else {
			throw new TransferFailException(String.format(USER_HAVE_NO_LINKED_ACCOUNT, event.getUserName()));
		}
	}
	
	@Override
	public void withdrawMoneyFromPlatform(WithdrawalOrderEvent event){
		User user = userService.findUser(event.getUserName());
		if (user.hasLinkedBankAccount()) {
			bankClient.transfer(user.getPlatformBankAccount(),
					user.getPrivateBankAccount(),
					event.getAmount().getValue().doubleValue());
		} else {
			throw new TransferFailException(String.format(USER_HAVE_NO_LINKED_ACCOUNT, event.getUserName()));
		}
	}
	
	public boolean hasEnoughFundsToPay(String userName, Money amount){
		return checkUserBalance(userName).isMoreOrEqual(amount);
	}

}
