package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.ScheduleNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Component
@AllArgsConstructor
@Transactional
public class LoanDomainServiceImpl implements LoanDomainService {
	private static final String LOAN_WITH_ID_NOT_FOUND = "Loan with id:%s not found.";
	private static final String INVALID_LOAN_STATUS = "Can not activate loan with status: %s";
	private static final String NO_SCHEDULE_FOR_LOAN_ID = "Repayment schedule for loan with id:%s, not found";
	private static final String NO_SCHEDULE_FOR_INVESTMENT_ID = "Repayment schedule for loan with id:%s, not found";
	
	private final LoanRepository loanRepository;
	private final RepaymentScheduleRepository scheduleRepository;
	private final LoanFactory loanFactory;
	private final InvestmentFactory investmentFactory;
	private final InvestmentRepository investmentRepository;
	
	/**
	 * Create loan and investments with UNCONFIRMED status
	 * and add to repository
	 * @return loan id
	 */
	@Override
	public Long createLoan(NewLoanParams params){
		Set<Investment> investments = investmentFactory.createInvestmentsFrom(params.getInvestmentsParams());
		Loan loan = loanFactory.createLoan(params, investments);
		Long loanId = loanRepository.save(loan);
//		for(Investment investment : investments){
//			investment.setLoanId(loanId);
//			Long id = investmentRepository.save(investment);
//			RepaymentSchedule schedule = investment.getSchedule();
//			schedule.setOwnerId(id);
//			scheduleRepository.save(schedule);
//		}
//		loan.getSchedule().setOwnerId(loanId);
//		scheduleRepository.save(loan.getSchedule());
		return loanId;
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
		RepaymentSchedule loanRepaymentSchedule = loan.getSchedule();
		loanRepaymentSchedule.reportRepayment();
		scheduleRepository.update(loanRepaymentSchedule.getId(), loanRepaymentSchedule);
		loan.getInvestments().stream()
				.map(Investment::getSchedule)
				.forEach(schedule ->{schedule.reportRepayment();
											scheduleRepository.update(schedule.getId(), schedule);});
		if(loanRepaymentSchedule.hasPaidAllScheduledRepayment()){
			loan.makeRepaid();
			loan.getInvestments().forEach(Investment::makeCompleted);
			loanRepository.update(loanId,loan);
		}
	}
	
	@Override
	public Optional<Repayment> findNextRepayment(Long loanId){
		Loan loan = findLoanById(loanId);
		RepaymentSchedule loanRepaymentSchedule = loan.getSchedule();
		return loanRepaymentSchedule.findNextRepayment();
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

	
	
}
