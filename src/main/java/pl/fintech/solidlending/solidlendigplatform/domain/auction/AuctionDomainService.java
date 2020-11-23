package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;

import java.time.Period;
import java.util.List;

public interface AuctionDomainService {
	Long createNewAuction(String username,
						  Period auctionDuration,
						  double loanAmount,
						  Period loanDuration,
						  double rate);
	
	boolean allowedToCreateAuction(Borrower auctionOwner);
	
	List<Auction> getUserAuctions(String userName);
	
	List<Auction> getPlatformAuctions();
	
	Long addOffer(Long auctionId,
				  String lenderName,
				  double amount,
				  double rate);
	
	List<Offer> getLenderOffers(String lenderName);
	EndAuctionEvent endAuction(Long auctionId, OffersSelectionPolicy offersSelectionPolicy);
}
