package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
@Service
@AllArgsConstructor
public class AuctionApplicationServiceImpl implements AuctionApplicationService {
	private AuctionDomainService domainService;
	private LoanApplicationService loanService;
	
	@Override
	public Long createLoanFromEndingAuction(Long auctionId, OffersSelectionPolicy selectionPolicy){
		Auction auction = domainService.endAuction(auctionId, selectionPolicy);
		return loanService.createLoan(auction);
	}
	
	@Override
	public List<Offer> getLenderOffers(String lenderName) {
		return domainService.getLenderOffers(lenderName);
	}
	
	@Override
	public Long addOffer(Offer domainOffer) {
		return domainService.addOffer(domainOffer);
	}
	
	@Override
	public List<Auction> getPlatformAuctions() {
		return domainService.getPlatformAuctions();
	}
	
	@Override
	public List<Auction> getUserAuctions(String borrowerName) {
		return domainService.getUserAuctions(borrowerName);
	}
	
	@Override
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
