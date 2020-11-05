package pl.fintech.solidlending.solidlendigplatform.domain.auction;

import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Borrower;

import java.time.Period;
import java.util.Collections;
@Component
public class AuctionFactory {
	
	public Auction creteAuction(String borrower, Period auctionDuration, LoanParams loanParams){
		return Auction.builder()
				.borrowerUserName(borrower)
				.auctionDuration(auctionDuration)
				.status(Auction.AuctionStatus.ACTIVE)
				.offers(Collections.emptySet())
				.loanParams(loanParams)
				.build();
				
	}
	
}
