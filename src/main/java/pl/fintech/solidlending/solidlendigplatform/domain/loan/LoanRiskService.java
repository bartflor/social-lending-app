package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import org.springframework.stereotype.Service;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;
@Service
public class LoanRiskService {
	public Risk estimateLoanRisk(Borrower borrower, Money loanAmount){
		//TODO: Implement
		return new Risk();
	}
}
