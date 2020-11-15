package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception;

public class AccountNotFoundException extends RuntimeException {
	public AccountNotFoundException(String message) {
		super(message);
	}
}
