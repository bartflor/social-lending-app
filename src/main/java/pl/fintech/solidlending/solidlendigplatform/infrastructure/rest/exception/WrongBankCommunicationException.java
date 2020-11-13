package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception;

public class WrongBankCommunicationException extends RuntimeException {
	public WrongBankCommunicationException(String message) {
		super(message);
	}
}
