package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.ScheduleNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Component
@AllArgsConstructor
public class LoanDomainServiceImpl implements LoanDomainService {
	private static final String LOAN_WITH_ID_NOT_FOUND = "Loan with id:%s not found.";
	private static final String LOAN_STATUS_FORBID_ACTIVATION = "Can not activate loan with status: %s";
	private static final String NO_SCHEDULE_FOR_LOAN_ID = "Repayment schedule for loan with id:%s, not found";
	
	
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
	 */
	@Override
	public Long activateLoan(Long loanId){
		Loan loan = loanRepository.findById(loanId)
				.orElseThrow(() -> new LoanNotFoundException(String.format(LOAN_WITH_ID_NOT_FOUND, loanId)));
		if(!loan.getStatus().equals(Loan.LoanStatus.UNCONFIRMED))
			throw new LoanCreationException(String.format(LOAN_STATUS_FORBID_ACTIVATION, loan.getStatus()));
		loanRepository.setActive(loanId);
		investmentRepository.setActiveWithLoanId(loanId);
		return loanId;
	}
	
	@Override
	public String repay(Long loanId) {
		return //TODO: maybe only update schedule?
	}
	
	@Override
	public Repayment findNextRepayment(Long loanId){
		RepaymentSchedule schedule = findLoanRepaymentSchedule(loanId);
		if(schedule.hasPaidAllScheduledRepayment()){
			return Optional.empty() // TODO: maby anoder result?
		}
		Optional<Repayment> repayment = schedule.findNextRepayment();
		return repayment;
	}
	
	
	@Override
	public Loan findLoanById(Long loanId) {
		return loanRepository.findById(loanId)
				.orElseThrow(() -> new LoanNotFoundException(String.format(LOAN_WITH_ID_NOT_FOUND, loanId)));
	}
	
	@Override
	public List<Loan> getUserLoans(String userName) {
		//TODO:check if usr is borrower
		return loanRepository.findAllByUsername(userName);
	}
	
	@Override
	public List<Investment> getUserInvestments(String userName) {
		//TODO:check if usr is lender
		return investmentRepository.findAllByUsername(userName);
	}
	
	@Override
	public RepaymentSchedule findLoanRepaymentSchedule(Long loanId) {
		return scheduleRepository.findRepaymentScheduleByLoanId(loanId)
				.orElseThrow(() -> new ScheduleNotFoundException(String.format(NO_SCHEDULE_FOR_LOAN_ID, loanId)));
	}
	
}
