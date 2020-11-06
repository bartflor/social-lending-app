package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public interface AuctionDomainService {
	Long createNewAuction(String username,
						  Period auctionDuration,
						  double loanAmount,
						  Period loanDuration,
						  double rate,
						  LocalDate loanStartDate);
	
	boolean allowedToCreateAuction(Borrower auctionOwner);
	
	List<Auction> getUserAuctions(String userName);
	
	List<Auction> getPlatformAuctions();
	
	Long addOffer(Offer offer);
	
	List<Offer> getLenderOffers(String lenderName);
}