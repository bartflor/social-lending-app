package pl.fintech.solidlending.solidlendigplatform.domain.auction.exception;

public class OfferNotFoundException extends RuntimeException {
	public OfferNotFoundException(String msg) {
		super(msg);
	}
}
