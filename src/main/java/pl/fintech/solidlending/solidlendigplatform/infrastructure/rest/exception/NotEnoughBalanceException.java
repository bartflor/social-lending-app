package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception;

public class NotEnoughBalanceException extends RuntimeException {
	public NotEnoughBalanceException(String message){
		super(message);
	}
	
}
