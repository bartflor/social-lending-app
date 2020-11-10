package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionLoanParams;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {
	private LoanDomainService domainService;
	
	
	/**
	 * this method create Loan, combining auctionLoanParam - proposed by borrower
	 * and selected offer/investment params - proposed by lenders.
	 * First implementation assumes, that all auctionLoanParams will be selected,
	 * except rate and value.
	 * @return - new loan id
	 */
	@Override
	public Long createLoan(Auction auction) {
		AuctionLoanParams auctionLoanParams = auction.getAuctionLoanParams();
		Set<Investment> investments = auction.getOffers().stream()
				.map(offer -> createInvestmentFromOffer(offer,
														auctionLoanParams.getLoanDuration()))
				.collect(Collectors.toSet());
		LoanParams loanParams = LoanParams.builder()
				.borrowerUserName(auction.getBorrowerUserName())
				.investments(investments)
				.loanAmount(auctionLoanParams.getLoanAmount())
				.loanDuration(auctionLoanParams.getLoanDuration())
				.loanStartDate(auctionLoanParams.getLoanStartDate())
				.build();
		return domainService.createLoan(loanParams);
	}
	
	private static Investment createInvestmentFromOffer(Offer offer, Period duration){
		Money value = offer.getAmount().addReturnRate(offer.getRate());
		return Investment.builder()
				.lenderName(offer.getLenderName())
				.startAmount(offer.getAmount())
				.value(offer.getAmount())
				.rate(offer.getRate())
				.duration(duration)
				.build();
	}
	
	@Override
	public Long activateLoan(Long loanId){
		
		//TODO:payment
		//TODO: Transfer service -> internal payment: lenders --borrower
		//Repayment schedule action??
//		loan.getInvestments().stream()
//				.forEach(investment -> transferService.makeInternalTransfer(investment.lenderName,
//						loan.borrowerUserName,
//						investment.getStartAmount().getValue().doubleValue()));
		return domainService.activateLoan(loanId);
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
