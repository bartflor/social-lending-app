package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.fintech.solidlending.solidlendigplatform.ExceptionMessageTemplate;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AddOfferException;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.UserNotFoundException;

@ControllerAdvice
public class ResponseExceptionHandler {
	
	@ExceptionHandler({AddOfferException.class, AuctionCreationException.class, UserNotFoundException.class})
	public ResponseEntity<Object> handleException(Exception exception){
		ExceptionMessageTemplate error = new ExceptionMessageTemplate(HttpStatus.NOT_FOUND, exception);
		return new ResponseEntity<>(error, error.getStatus());
	}
}
