package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanNotFoundException;

import java.util.List;
import java.util.Set;
@Component
@AllArgsConstructor
public class LoanDomainServiceImpl implements LoanDomainService {
	private static final String LOAN_WITH_ID_NOT_FOUND = "Loan with id:%s not found.";
	private static final String LOAN_STATUS_FORBID_ACTIVATION = "Can not activate loan with status: %s";
	
	
	private LoanRepository loanRepository;
	private RepaymentScheduleRepository scheduleRepository;
	private LoanFactory loanFactory;
	private InvestmentRepository investmentRepository;
	
	/**
	 * Create loan and investments with UNCONFIRMED status
	 * and add to repository
	 * @return loan id
	 */
	@Override
	public Long createLoan(LoanParams params){
		Loan loan = loanFactory.createLoan(params);
		Long loanId = loanRepository.save(loan);
		Set<Investment> investments = loan.getInvestments();
		investments.forEach(investment -> investment.setLoanId(loanId));
		investments.forEach(investmentRepository::save);
		RepaymentSchedule schedule = loan.getSchedule();
		schedule.setLoanId(loanId);
		scheduleRepository.save(schedule);
		return loan.getId();
	}
	
	/**
	 * update loan and investment status
	 * and make money transfer
	 */
	@Override
	public Long activateLoan(Long loanId){
		Loan loan = loanRepository.findById(loanId)
				.orElseThrow(() -> new LoanNotFoundException(String.format(LOAN_WITH_ID_NOT_FOUND, loanId)));
		if(!loan.getStatus().equals(Loan.LoanStatus.UNCONFIRMED))
			throw new LoanCreationException(String.format(LOAN_STATUS_FORBID_ACTIVATION, loan.getStatus()));
		loanRepository.setActive(loanId);
		investmentRepository.setActiveWithLoanId(loanId);
		//TODO: Transfer service -> internal payment: lenders --> borrower
		//Repayment schedule action??
		return loanId;
	}
	
	@Override
	public void repay(Long loanId){
		//TODO: transfer, update schedule
	}
	
	
	@Override
	public Loan findLoanById(Long loanId) {
		return loanRepository.findById(loanId)
				.orElseThrow(() -> new LoanNotFoundException(String.format(LOAN_WITH_ID_NOT_FOUND, loanId)));
	}
	
	@Override
	public List<Loan> getUserLoans(String userName) {
		return loanRepository.findAllByUsername(userName);
	}
	
	@Override
	public List<Investment> getUserInvestments(String userName) {
		return investmentRepository.findAllByUsername(userName);
	}
	
}
