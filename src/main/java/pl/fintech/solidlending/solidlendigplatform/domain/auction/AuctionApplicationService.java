package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public interface AuctionApplicationService {
	Long createLoanFromEndingAuction(Long auctionId, OffersSelectionPolicy selectionPolicy);
	
	List<Offer> getLenderOffers(String lenderName);
	
	Long addOffer(Offer domainOffer);
	
	List<Auction> getPlatformAuctions();
	
	List<Auction> getUserAuctions(String borrowerName);
	
	Long createNewAuction(String borrower,
						  Period ofMonths,
						  double amount,
						  Period ofDays,
						  double rate);
}
