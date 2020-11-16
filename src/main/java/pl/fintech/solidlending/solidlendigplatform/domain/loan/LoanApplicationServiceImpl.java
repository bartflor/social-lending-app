package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionLoanParams;
import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.TimeService;
import pl.fintech.solidlending.solidlendigplatform.domain.common.TransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.RepaymentNotExecuted;
import pl.fintech.solidlending.solidlendigplatform.domain.payment.TransferService;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {
	private static final String LOAN_REPAID = "No repayment left in schedule. Loan with id: %s is repaid";
	private static final String INVESTMENT_REPAID = "No repayment left in schedule. Investment with id: %s is repaid";
	private static final String LOAN_NOT_ACTIVE = "Can not repay not ACTIVE loan with id: %s";
	private LoanDomainService domainService;
	private TransferService transferService;
	private TimeService timeService;
	
	/**
	 * this method create newLoanParams, combining auctionLoanParam - proposed by borrower
	 * and selected offer params - proposed by lenders.
	 * Domain service creates new loan using composed newLoanParams.
	 * @return - new loan id
	 */
	@Override
	public Long createLoan(EndAuctionEvent endAuctionEvent) {
		AuctionLoanParams auctionLoanParams = endAuctionEvent.getAuctionLoanParams();
		Instant loanStartDate = timeService.now();
		List<NewInvestmentParams> investmentsParamsList =
			endAuctionEvent.getOffers().stream()
				.map(offer ->
						NewInvestmentParams.builder()
							.LenderUserName(offer.getLenderName())
							.BorrowerName(endAuctionEvent.getBorrowerUserName())
							.investedMoney(offer.getAmount())
							.returnRate(offer.getRate())
							.risk(auctionLoanParams.getLoanRisk())
							.investmentDuration(auctionLoanParams.getLoanDuration())
							.investmentStartDate(loanStartDate)
							.build())
				.collect(Collectors.toList());
		NewLoanParams newLoanParams = NewLoanParams.builder()
				.borrowerUserName(endAuctionEvent.getBorrowerUserName())
				.investmentsParams(investmentsParamsList)
				.loanAmount(auctionLoanParams.getLoanAmount())
				.loanDuration(auctionLoanParams.getLoanDuration())
				.loanStartDate(loanStartDate)
				.build();
		return domainService.createLoan(newLoanParams);
	}
	
	/**
	 * Activate loan with @param loanId
	 * request money transfer lenders -> borrower
	 */
	@Override
	public Long activateLoan(Long loanId){
		Loan loan = findLoanById(loanId);
		List<TransferOrderEvent> transferOrderEventsList = loan.getInvestments().stream()
				.map(investment -> TransferOrderEvent.builder()
						.targetUserName(loan.getBorrowerUserName())
						.sourceUserName(investment.lenderName)
						.amount(investment.getLoanAmount())
						.build())
				.collect(Collectors.toList());
		transferService.execute(transferOrderEventsList);
		return domainService.activateLoan(loanId);
	}
	@Override
	public RepaymentSchedule getRepaymentScheduleByLoanId(Long loanId){
		return domainService.findLoanRepaymentSchedule(loanId);
	}
	
	@Override
	public RepaymentSchedule getInvestmentSchedule(Long investmentId){
		return domainService.findInvestmentRepaymentSchedule(investmentId);
	}
	
	@Override
	public void repayLoan(Long loanId){
		Loan loan = findLoanById(loanId);
		if(!loan.isActive()){
			throw new RepaymentNotExecuted(String.format(LOAN_NOT_ACTIVE, loanId));
		}
		if(loan.getSchedule().hasPaidAllScheduledRepayment()){
			throw new RepaymentNotExecuted(String.format(LOAN_REPAID, loanId));
		}
		Set<Investment> investments = loan.getInvestments();
		for(Investment investment : investments){
			Repayment repayment = investment.getSchedule().findNextRepayment()
					.orElseThrow(() -> new RepaymentNotExecuted(String.format(INVESTMENT_REPAID, investment.getInvestmentId())));
			TransferOrderEvent transferOrder = TransferOrderEvent.builder()
					.targetUserName(investment.lenderName)
					.sourceUserName(loan.getBorrowerUserName())
					.amount(repayment.getValue())
					.build();
			transferService.execute(transferOrder);
		}
		//TODO:confirm repayment
		domainService.reportRepayment(loanId);
	}
	
	@Override
	public Loan findLoanById(Long loanId) {
		return domainService.findLoanById(loanId);
	}
	
	@Override
	public List<Loan> getUserLoans(String userName) {
		return domainService.getUserLoans(userName);
	}
	
	@Override
	public List<Investment> getUserInvestments(String userName) {
		return domainService.getUserInvestments(userName);
	}
}
