package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentSchedule;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
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
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/loans/{loanId}")
	public Loan getLoanWithId(@PathVariable long loanId){
		return loanApplicationService.findLoanById(loanId);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/loans/{loanId}/repayment-schedule")
	public RepaymentSchedule getLoanScheduleWithId(@PathVariable long loanId){
		return loanApplicationService.getRepaymentScheduleByLoanId(loanId);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping("/loans/{loanId}/repay")
	public void repayLoanWithId(@PathVariable long loanId){
		loanApplicationService.repayLoan(loanId);
	}
	
	
	
	
	
}
