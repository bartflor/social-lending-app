package pl.fintech.solidlending.solidlendigplatform.domain.payment.exception;

public class TransferFailException extends IllegalArgumentException {
	public TransferFailException(String message) {
		super(message);
	}
}
