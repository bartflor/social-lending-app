package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception;

public class AcountNotFoundException extends RuntimeException {
	public AcountNotFoundException(String s) {
		super(s);
	}
}
