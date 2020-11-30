package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AddOfferException;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.OfferNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.exception.ValueNotAllowedException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanCreationException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.ScheduleNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.AccountNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.BankCommunicationFailedException;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.TransferNotCreatedException;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.UnprocessableRequestException;

@Log
@ControllerAdvice
public class ResponseExceptionHandler {
	
	@ExceptionHandler({UserNotFoundException.class,
			LoanNotFoundException.class,
			AuctionNotFoundException.class,
			OfferNotFoundException.class,
			UserNotFoundException.class,
			ScheduleNotFoundException.class,
			AccountNotFoundException.class })
	public ResponseEntity<Object> handleNotFoundException(Exception exception){
		ExceptionMessageTemplate error = new ExceptionMessageTemplate(HttpStatus.NOT_FOUND, exception);
		log.warning(exception.toString());
		return new ResponseEntity<>(error, error.getStatus());
	}
	
	
	@ExceptionHandler({AuctionCreationException.class,
			AddOfferException.class,
			LoanCreationException.class,
			ValueNotAllowedException.class,
			TransferNotCreatedException.class})
	public ResponseEntity<Object> handleBadRequestException(Exception exception){
		ExceptionMessageTemplate error = new ExceptionMessageTemplate(HttpStatus.BAD_REQUEST, exception);
		log.warning(exception.toString());
		return new ResponseEntity<>(error, error.getStatus());
	}
	
	@ExceptionHandler({UnprocessableRequestException.class,
			BankCommunicationFailedException.class})
	public ResponseEntity<Object> handleUnprocessableRequest(Exception exception){
		ExceptionMessageTemplate error = new ExceptionMessageTemplate(HttpStatus.UNPROCESSABLE_ENTITY, exception);
		log.warning(exception.toString());
		return new ResponseEntity<>(error, error.getStatus());
	}
}
