package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.fintech.solidlending.solidlendigplatform.ExceptionMessageTemplate;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AddOfferException;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanNotFoundException;

@ControllerAdvice
public class ResponseExceptionHandler {
	
	@ExceptionHandler({AddOfferException.class, UserNotFoundException.class, LoanNotFoundException.class})
	public ResponseEntity<Object> handleNotFoundException(Exception exception){
		ExceptionMessageTemplate error = new ExceptionMessageTemplate(HttpStatus.NOT_FOUND, exception);
		return new ResponseEntity<>(error, error.getStatus());
	}
	
	
	@ExceptionHandler({AuctionCreationException.class, LoanCreationException.class})
	public ResponseEntity<Object> handleBadRequestException(Exception exception){
		ExceptionMessageTemplate error = new ExceptionMessageTemplate(HttpStatus.BAD_REQUEST, exception);
		return new ResponseEntity<>(error, error.getStatus());
	}
}
