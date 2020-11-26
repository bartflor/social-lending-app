package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;
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
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/{investmentId}/giveOpinion")
	public void giveOpinionOnBorrower(@PathVariable Long investmentId, @RequestBody OpinionDto opinionDto){
		loanApplicationService.giveOpinionOnBorrower(Opinion.makeOpinion(opinionDto.getAuthor(),
				opinionDto.getOpinionText(),
				opinionDto.getOpinionRating(),
				investmentId));
	}
}
