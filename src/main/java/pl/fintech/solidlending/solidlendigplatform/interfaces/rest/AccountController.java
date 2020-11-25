package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.UserService;
import pl.fintech.solidlending.solidlendigplatform.domain.payment.PaymentService;

import java.util.Map;

import static pl.fintech.solidlending.solidlendigplatform.domain.common.ExternalTransferOrderEvent.TransferType;

@RequestMapping("api/accounts")
@RestController
@AllArgsConstructor
public class AccountController {
	private UserService userService;
	private PaymentService paymentService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{userName}")
	public UserDetailsDto getUserDetails(@PathVariable String userName){
		return UserDetailsDto.from(userService.getUserDetails(userName),
				paymentService.checkUserBalance(userName).getValue());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping("/{userName}")
	public void updateUserDetails(@PathVariable String userName, @RequestBody Map<String, String> newDetails){
		userService.partialUpdateUserDetails(userName, newDetails);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/deposit")
	public void depositAmountInPlatform(@RequestBody TransferDto depositTransfer){
		paymentService.executeExternal(depositTransfer.createTransferOrderEvent(TransferType.DEPOSIT));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/withdrawal")
	public void withdrawalAmountFromPlatform(@RequestBody TransferDto withdrawalTransfer){
		paymentService.executeExternal(withdrawalTransfer.createTransferOrderEvent(TransferType.WITHDRAWAL));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{borrowerName}/borrowerOpinion")
	public BorrowerInfoDto getBorrowerInfo(@PathVariable String borrowerName){
		return BorrowerInfoDto.from(userService.findBorrower(borrowerName));
	}
	
}
