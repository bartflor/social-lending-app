package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;

@Component
public class AuctionFactory {
	
	public Auction creteAuction(String borrower, Period auctionDuration, LoanParams loanParams){
		return Auction.builder()
				.borrowerUserName(borrower)
				.startDate(LocalDate.now())
				.auctionDuration(auctionDuration)
				.status(Auction.AuctionStatus.ACTIVE)
				.offers(new HashSet<>())
				.loanParams(loanParams)
				.build();
				
	}
	
}
