package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import feign.Response;
import feign.codec.ErrorDecoder;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.AcountNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.NotEnoughBalanceException;

public class FeignExceptionHandler implements ErrorDecoder {
	@Override
	public Exception decode(String s, Response response) {
		if(response.status() == 422 && response.body().toString().contains("Transaction rejected - the source account does not have sufficient cash"))
			return new NotEnoughBalanceException(response.toString());
		if(response.status() == 404 && response.body().toString().contains("Not found account with account number"))
			return new AcountNotFoundException(response.toString());
		return new RuntimeException(s+response.status());
	}
}
