package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loans")
@AllArgsConstructor
public class LoanController {
	LoanApplicationService loanApplicationService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{userName}")
	public List<LoanDto> getUserLoans(@PathVariable String userName){
		return loanApplicationService.getUserLoans(userName).stream()
				.map(LoanDto::from)
				.collect(Collectors.toList());
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping("/{loanId}/activate")
	public void activateLoan(@PathVariable long loanId){
		loanApplicationService.activateLoan(loanId);
	}
	
	
}
