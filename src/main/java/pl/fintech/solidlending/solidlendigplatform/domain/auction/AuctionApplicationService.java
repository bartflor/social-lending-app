package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import java.time.Period;
import java.util.List;

public interface AuctionApplicationService {
	Long createLoanFromEndingAuction(Long auctionId, OffersSelectionPolicy selectionPolicy);
	
	List<Offer> getLenderOffers(String lenderName);
	
	Long addOffer(Long auctionId, String lenderName, double amount, double rate, Boolean allowAmountSplit);
	
	List<Auction> getPlatformAuctions();
	
	List<Auction> getUserAuctions(String borrowerName);
	
	Long createNewAuction(String borrower,
						  Period auctionDuration,
						  double amount,
						  Period loanDuration,
						  double rate);
}
