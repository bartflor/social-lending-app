package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.DepositOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.TransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.UserService;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails;
import pl.fintech.solidlending.solidlendigplatform.domain.payment.PaymentService;

import java.util.UUID;

@RequestMapping("api/accounts")
@RestController
@AllArgsConstructor
public class AccountController {
	private UserService userService;
	private PaymentService paymentService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{userName}")
	public UserDetailsDto getUserDetails(@PathVariable String userName){
		return UserDetailsDto.from(userService.getUserDetails(userName));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/deposit")
	public void depositAmountInPlatform(TransferDto depositTransfer){
		paymentService.depositMoneyIntoPlatform(TransferDto.depositOrder(depositTransfer));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/withdrawal")
	public void withdrawalAmountFromPlatform(TransferDto withdrawalTransfer){
		paymentService.depositMoneyIntoPlatform(TransferDto.widthdrawalOrder(withdrawalTransfer));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/{userName}/link-account")
	public void addPrivateBankAccount(@PathVariable String userName, UUID accountNumber){
		paymentService.depositMoneyIntoPlatform(DepositOrderEvent.builder().build());
	}
}
