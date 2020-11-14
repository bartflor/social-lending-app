package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception;

public class BankCommunicationFailedException extends RuntimeException {
	public BankCommunicationFailedException(String message) {
		super(message);
	}
}
