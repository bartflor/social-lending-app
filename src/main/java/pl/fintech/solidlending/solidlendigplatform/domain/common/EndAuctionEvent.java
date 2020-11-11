package pl.fintech.solidlending.solidlendigplatform.domain.common;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionLoanParams;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;

import java.util.Set;
@Builder
@Value
public class EndAuctionEvent {
	String BorrowerUserName;
	Set<Offer> offers;
	AuctionLoanParams auctionLoanParams;
}
