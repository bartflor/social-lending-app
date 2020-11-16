package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/investments")
@AllArgsConstructor
public class InvestmentController {
	LoanApplicationService loanApplicationService;
	
	@GetMapping("/{userName}")
	public List<InvestmentDto> getAllUserInvestments(@PathVariable String userName){
    return loanApplicationService.getUserInvestments(userName).stream()
        .map(InvestmentDto::from)
        .collect(Collectors.toList());
	}
}
