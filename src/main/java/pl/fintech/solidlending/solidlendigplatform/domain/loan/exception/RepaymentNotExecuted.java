package pl.fintech.solidlending.solidlendigplatform.domain.loan.exception;

public class RepaymentNotExecuted extends RuntimeException {
	public RepaymentNotExecuted(String message) {
		super(message);
	}
}
