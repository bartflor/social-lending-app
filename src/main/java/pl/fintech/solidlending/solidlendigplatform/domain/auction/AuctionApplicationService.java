package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
@Service
@AllArgsConstructor
public class AuctionApplicationService {
	private AuctionDomainService domainService;
	private LoanApplicationService loanService;
	
	public Long createLoanFromEndingAuction(Long auctionId, OffersSelectionPolicy selectionPolicy){
		Auction auction = domainService.endAuction(auctionId, selectionPolicy);
		return loanService.createLoan(auction);
	}
	
	public List<Offer> getLenderOffers(String lenderName) {
		return domainService.getLenderOffers(lenderName);
	}
	
	public Long addOffer(Offer domainOffer) {
		return domainService.addOffer(domainOffer);
	}
	
	public List<Auction> getPlatformAuctions() {
		return domainService.getPlatformAuctions();
	}
	
	public List<Auction> getUserAuctions(String borrowerName) {
		return domainService.getUserAuctions(borrowerName);
	}
	
	public Long createNewAuction(String borrower,
								 Period ofMonths,
								 double amount,
								 Period ofDays,
								 double rate,
								 LocalDate loanStartDate) {
		return domainService.createNewAuction( borrower,
				ofMonths,
				amount,
				ofDays,
				rate,
				loanStartDate);
	}
}
