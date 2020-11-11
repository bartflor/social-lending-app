package pl.fintech.solidlending.solidlendigplatform.domain.loan.exception;

public class LoanNotFoundException extends RuntimeException {
	public LoanNotFoundException(String msg) {
		super(msg);
	}
}
