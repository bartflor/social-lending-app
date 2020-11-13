package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionLoanParams;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.payment.TransferService;

import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {
	private LoanDomainService domainService;
	private TransferService transferService;
	
	/**
	 * this method create Loan, combining auctionLoanParam - proposed by borrower
	 * and selected offer/investment params - proposed by lenders.
	 * First implementation assumes, that all auctionLoanParams will be selected,
	 * except rate and value.
	 * @return - new loan id
	 */
	@Override
	public Long createLoan(EndAuctionEvent endAuctionEvent) {
		AuctionLoanParams auctionLoanParams = endAuctionEvent.getAuctionLoanParams();
		Set<Investment> investments = endAuctionEvent.getOffers().stream()
				.map(offer -> createInvestmentFromOffer(offer,
														auctionLoanParams.getLoanDuration()))
				.collect(Collectors.toSet());
		LoanParams loanParams = LoanParams.builder()
				.borrowerUserName(endAuctionEvent.getBorrowerUserName())
				.investments(investments)
				.loanAmount(auctionLoanParams.getLoanAmount())
				.loanDuration(auctionLoanParams.getLoanDuration())
				.loanStartDate(auctionLoanParams.getLoanStartDate())
				.build();
		return domainService.createLoan(loanParams);
	}
	
	private static Investment createInvestmentFromOffer(Offer offer, Period duration){
		Money value = offer.getAmount().calculateValueWithReturnRate(offer.getRate());
		return Investment.builder()
				.lenderName(offer.getLenderName())
				.startAmount(offer.getAmount())
				.value(value)
				.rate(offer.getRate())
				.duration(duration)
				.build();
	}
	
	/**
	 * Activate loan with @param loanId
	 * make money transfer lenders -> borrower
	 */
	@Override
	public Long activateLoan(Long loanId){
		Loan loan = findLoanById(loanId);
		loan.getInvestments().stream()
				.forEach(investment -> transferService.makeInternalTransfer(
						investment.getLenderName(),
						loan.getBorrowerUserName(),
						investment.getStartAmount()));
		return domainService.activateLoan(loanId);
	}
	@Override
	public RepaymentSchedule getRepaymentScheduleByLoanId(Long loanId){
		return domainService.findLoanRepaymentSchedule(loanId);
	}
	@Override
	public String repayLoan(Long loanId){
		Loan loan = findLoanById(loanId);
		Money amount = //TODO get from schedule
		
		loan.getInvestments().stream()
				.forEach(investment -> transferService.makeInternalTransfer(
						loan.getBorrowerUserName(),
						investment.getLenderName(),
						amount));
		return domainService.repay(loanId);
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
