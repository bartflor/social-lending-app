package pl.fintech.solidlending.solidlendigplatform;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
public class ExceptionMessageTemplate {
	
	private HttpStatus status;
	private String message;
	private Instant dateTime;
	
	private ExceptionMessageTemplate(){
		this.dateTime = Instant.now();
	}
	
	public ExceptionMessageTemplate(HttpStatus status, Exception exception) {
		this();
		this.status = status;
		this.message = exception.getMessage();
	}
}