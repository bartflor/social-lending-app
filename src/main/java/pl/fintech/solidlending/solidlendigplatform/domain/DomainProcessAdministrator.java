package pl.fintech.solidlending.solidlendigplatform.domain;

import org.springframework.context.event.EventListener;
import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService;

public class DomainProcessAdministrator {
	LoanApplicationService loanApplicationService;
	
	@EventListener
	public void handle(EndAuctionEvent endAuctionEvent){
		loanApplicationService.createLoan(endAuctionEvent);
	}
}
