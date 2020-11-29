package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.UserService;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.RepaymentException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
@Component
@AllArgsConstructor
@Transactional
class LoanDomainServiceImpl implements LoanDomainService {
	private static final String LOAN_WITH_ID_NOT_FOUND = "Loan with id:%s not found.";
	private static final String INVALID_ACTIVATION_LOAN_STATUS = "Can not activate loan with id: %s, loan status must be UNCONFIRMED";
	private static final String INVALID_REJECTION_LOAN_STATUS = "Can not reject loan with id: %s, loan status must be UNCONFIRMED";
	private static final String LOAN_REPAID = "No repayment left in schedule. Loan with id: %s is repaid";
	private static final String LOAN_NOT_ACTIVE = "Can not repay not ACTIVE loan with id: %s";
	
	private final LoanRepository loanRepository;
	private final RepaymentScheduleRepository scheduleRepository;
	private final LoanFactory loanFactory;
	private final InvestmentFactory investmentFactory;
	private final InvestmentRepository investmentRepository;
	private final UserService userService;
	
	/**
	 * Create loan and investments with UNCONFIRMED status
	 * and add to repository
	 * @return loan id
	 */
	@Override
	public Long createLoan(NewLoanParams params){
		Set<Investment> investments = investmentFactory.createInvestmentsFrom(params.getInvestmentsParams());
		Loan loan = loanFactory.createLoan(params, investments);
		return loanRepository.save(loan);
	}
	
	/**
	 * update loan and investment status
	 */
	@Override
	public Long activateLoan(Long loanId){
		if(!checkLoanStatus(loanId, Loan.LoanStatus.UNCONFIRMED)){
			throw new LoanCreationException(String.format(INVALID_ACTIVATION_LOAN_STATUS, loanId));
		}
		loanRepository.setActive(loanId);
		investmentRepository.setActiveWithLoanId(loanId);
		return loanId;
	}
	
	@Override
	public void rejectLoan(Long loanId) {
		if(!checkLoanStatus(loanId, Loan.LoanStatus.UNCONFIRMED)){
			throw new LoanCreationException(String.format(INVALID_REJECTION_LOAN_STATUS, loanId));
		}
		loanRepository.delete(loanId);
	}
	
	@Override
	public boolean checkLoanStatus(Long loanId, Loan.LoanStatus status){
		Loan loan = loanRepository.findById(loanId)
				.orElseThrow(() -> new LoanNotFoundException(String.format(LOAN_WITH_ID_NOT_FOUND, loanId)));
		return loan.getStatus().equals(status);
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
	public Set<Investment> getLoanInvestmentsForRepayment(Long loanId){
		Loan loan = findLoanById(loanId);
		if(!loan.isActive()){
			throw new RepaymentException(String.format(LOAN_NOT_ACTIVE, loanId));
		}
		if(loan.getSchedule().hasPaidAllScheduledRepayment()){
			throw new RepaymentException(String.format(LOAN_REPAID, loanId));
		}
		return loan.getInvestments();
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
	
	@Override
	public void giveOpinionOnBorrower(Opinion opinion){
		Investment investment = investmentRepository.findById(opinion.getInvestmentId())
				.orElseThrow(() -> new LoanNotFoundException(String.format(LOAN_WITH_ID_NOT_FOUND, opinion.getInvestmentId())));
		userService.giveOpinionOnBorrower(investment.getBorrowerName(), opinion);
		
	}

	
	
}
