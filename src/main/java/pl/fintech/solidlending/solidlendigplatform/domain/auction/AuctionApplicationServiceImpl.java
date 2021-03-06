package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.EndAuctionEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService;

import java.time.Period;
import java.util.List;
@Service
@AllArgsConstructor


class AuctionApplicationServiceImpl implements AuctionApplicationService {
	private AuctionDomainService domainService;
	private LoanApplicationService loanService;
	
	@Override
	public Long createLoanFromEndingAuction(Long auctionId, OffersSelectionPolicy selectionPolicy){
		EndAuctionEvent endAuctionEvent = domainService.endAuction(auctionId, selectionPolicy);
		return loanService.createLoan(endAuctionEvent);
	}
	
	@Override
	public List<Offer> getLenderOffers(String lenderName) {
		return domainService.getLenderOffers(lenderName);
	}
	
	@Override
	public Long addOffer(Long auctionId, String lenderName, double amount, double rate) {
		return domainService.addOffer(auctionId,lenderName,  amount, rate);
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
								 Period auctionDuration,
								 double amount,
								 Period loanDuration,
								 double rate) {
		return domainService.createNewAuction(borrower,
				auctionDuration,
				amount,
				loanDuration,
				rate);
	}
	
	@Override
	public void deleteAuction(Long auctionId) {
		domainService.deleteAuction(auctionId);
	}
	
	@Override
	public void deleteOffer(Long offerId) {
		domainService.deleteOffer(offerId);
	}
}
