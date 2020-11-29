package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.solidlending.solidlendigplatform.domain.common.TimeService;
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.EndAuctionEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.TransferOrderEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;
import pl.fintech.solidlending.solidlendigplatform.domain.payment.PaymentService;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
@Transactional
class LoanApplicationServiceImpl implements LoanApplicationService {
	private final LoanDomainService domainService;
	private final PaymentService paymentService;
	private final TimeService timeService;
	
	/**
	 * Map application common object: endAuctionEvent to domain specific: newLoanParams,
	 * combining auctionLoanParam - proposed by borrower, and params of selected - proposed by lenders.
	 * Domain service creates new loan using composed newLoanParams.
	 * @return - new loan id
	 */
	@Override
	public Long createLoan(EndAuctionEvent endAuctionEvent) {
		Instant loanStartDate = timeService.now();
		List<NewInvestmentParams> investmentsParamsList =
			endAuctionEvent.getOfferParamsSet().stream()
				.map(offerParams ->
						NewInvestmentParams.builder()
							.LenderUserName(offerParams.getLenderName())
							.BorrowerName(endAuctionEvent.getBorrowerUserName())
							.investedMoney(offerParams.getAmount())
							.returnRate(offerParams.getRate())
							.investmentDuration(endAuctionEvent.getLoanDuration())
							.investmentStartDate(loanStartDate)
							.build())
				.collect(Collectors.toList());
		NewLoanParams newLoanParams = NewLoanParams.builder()
				.borrowerUserName(endAuctionEvent.getBorrowerUserName())
				.investmentsParams(investmentsParamsList)
				.loanAmount(endAuctionEvent.getLoanAmount())
				.loanDuration(endAuctionEvent.getLoanDuration())
				.loanStartDate(loanStartDate)
				.build();
		return domainService.createLoan(newLoanParams);
	}
	
	/**
	 * build event object for money transfer request: lenders -> borrower, and call paymentService
	 * If payment end successfully, domainService will activate loan with loanId.
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
		paymentService.execute(transferOrderEventsList);
		return domainService.activateLoan(loanId);
	}
	
	@Override
	public void rejectLoanProposal(Long loanId){
		domainService.rejectLoan(loanId);
	}
	
	/**
	 * build event object for money transfer request: borrower -> lenders, and call paymentService
	 * If payment end successfully, domainService will report it in repayment schedule.
	 */
	@Override
	public void repayLoan(Long loanId){
    	for (Investment investment : domainService.getLoanInvestmentsForRepayment(loanId)) {
			Repayment repayment = investment.getSchedule().getNextRepayment();
			TransferOrderEvent transferOrder = TransferOrderEvent.builder()
					.targetUserName(investment.lenderName)
					.sourceUserName(investment.getBorrowerName())
					.amount(repayment.getValue())
					.build();
			paymentService.execute(transferOrder);
		}
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
	
	@Override
	public void giveOpinionOnBorrower(Opinion opinion){
		domainService.giveOpinionOnBorrower(opinion);
	}
}
