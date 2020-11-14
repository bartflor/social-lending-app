package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception;

public class UnprocessableRequestException extends RuntimeException {
	public UnprocessableRequestException(String message){
		super(message);
	}
	
}
