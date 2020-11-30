package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
public class ExceptionMessageTemplate {
	
	private HttpStatus status;
	private String message;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Warsaw")
	private Instant dateTime;
	
	public ExceptionMessageTemplate(HttpStatus status, Exception exception) {
		this.dateTime = Instant.now();
		this.status = status;
		this.message = exception.getMessage();
	}
}