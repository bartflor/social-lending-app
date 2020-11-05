package pl.fintech.solidlending.solidlendigplatform.domain.auction.exception;

public class AuctionNotFoundException extends RuntimeException {
	public AuctionNotFoundException(String msg) {
		super(msg);
	}
}
