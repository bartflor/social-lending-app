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
	private static final String INVALID_LOAN_STATUS = "Can not activate loan with status: %s";
	private static final String NO_SCHEDULE_FOR_LOAN_ID = "Repayment schedule for loan with id:%s, not found";
	private static final String NO_SCHEDULE_FOR_INVESTMENT_ID = "Repayment schedule for loan with id:%s, not found";
	
	private final LoanRepository loanRepository;
	private final RepaymentScheduleRepository scheduleRepository;
	private final LoanFactory loanFactory;
	private final InvestmentRepository investmentRepository;
	
	/**
	 * Create loan and investments with UNCONFIRMED status
	 * and add to repository
	 * @return loan id
	 */
	@Override
	public Long createLoan(NewLoanParams params){
		Loan loan = loanFactory.createLoan(params);
		Long loanId = loanRepository.save(loan);
		Set<Investment> investments = loan.getInvestments();
		for(Investment investment : investments){
			investment.setLoanId(loanId);
			Long id = investmentRepository.save(investment);
			RepaymentSchedule schedule = investment.getSchedule();
			schedule.setOwnerId(id);
			scheduleRepository.save(schedule);
		}
		loan.getSchedule().setOwnerId(loanId);
		scheduleRepository.save(loan.getSchedule());
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
			throw new LoanCreationException(String.format(INVALID_LOAN_STATUS, loan.getStatus()));
		loanRepository.setActive(loanId);
		investmentRepository.setActiveWithLoanId(loanId);
		return loanId;
	}
	
	@Override
	public void reportRepayment(Long loanId) {
		Loan loan = findLoanById(loanId);
		RepaymentSchedule loanRepaymentSchedule = findLoanRepaymentSchedule(loanId);
		loanRepaymentSchedule.reportRepayment();
		scheduleRepository.update(loanRepaymentSchedule.getId(), loanRepaymentSchedule);
		loan.getInvestments().stream()
				.map(Investment::getSchedule)
				.forEach(schedule ->{schedule.reportRepayment();
											scheduleRepository.update(schedule.getId(), schedule);});
	}
	
	@Override
	public Optional<Repayment> findNextRepayment(Long loanId){
		RepaymentSchedule schedule = findLoanRepaymentSchedule(loanId);
		return schedule.findNextRepayment();
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
	
	@Override
	public RepaymentSchedule findInvestmentRepaymentSchedule(Long investmentId) {
		return scheduleRepository.findRepaymentScheduleByInvestmentId(investmentId)
				.orElseThrow(() -> new ScheduleNotFoundException(String.format(NO_SCHEDULE_FOR_INVESTMENT_ID, investmentId)));
	}
	
}
