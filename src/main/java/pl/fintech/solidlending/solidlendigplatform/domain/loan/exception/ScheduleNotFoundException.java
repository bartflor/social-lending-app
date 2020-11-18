package pl.fintech.solidlending.solidlendigplatform.domain.loan.exception;

public class ScheduleNotFoundException extends RuntimeException {
	public ScheduleNotFoundException(String message) {
		super(message);
	}
}
