package pl.fintech.solidlending.solidlendigplatform.domain.common.values.exception;

public class ValueNotAllowedException extends IllegalArgumentException {
	public ValueNotAllowedException(String message) {
		super(message);
	}
}
