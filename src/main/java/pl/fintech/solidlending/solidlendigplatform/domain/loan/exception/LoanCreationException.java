package pl.fintech.solidlending.solidlendigplatform.domain.loan.exception;

public class LoanCreationException extends RuntimeException {
	public LoanCreationException(String msg) {
		super(msg);
	}
}
