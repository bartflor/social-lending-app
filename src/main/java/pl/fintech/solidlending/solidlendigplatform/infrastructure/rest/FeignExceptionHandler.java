package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.json.JSONObject;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.AccountNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.BankCommunicationFailedException;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.TransferNotCreatedException;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.UnprocessableRequestException;
@Log
@AllArgsConstructor
public class FeignExceptionHandler implements ErrorDecoder {
	
	@Override
	public Exception decode(String exceptionSource, Response response) {
		String messageDetails = getResponseBodyField(response, "details");
		
		if(response.status() == 422){
			log.warning("Unprocessable bank API request: "+messageDetails+
					"\n Exception at: "+exceptionSource);
			throw new UnprocessableRequestException(messageDetails);
		}
		if (response.status() == 404) {
			log.severe("NotFound exception for bank API request: "+messageDetails+
					"\n Exception at: "+exceptionSource);
			return new AccountNotFoundException(messageDetails);
		}
		if (response.status() == 400) {
			log.severe("Bad Request exception for bank API request: "+messageDetails+
					"\n Exception at: "+exceptionSource);
			return new TransferNotCreatedException(messageDetails);
		}
		log.severe("Unknown exception occur during communication with bank API: "+messageDetails+
			"\n Exception at: "+exceptionSource);
		return new BankCommunicationFailedException(messageDetails);
	}
	
	private String getResponseBodyField(Response response, String field) {
		JSONObject responseBody = new JSONObject();
		if(response.body() != null){
			responseBody = new JSONObject(response.body().toString());
		}
		return responseBody.has(field) ? responseBody.get(field).toString() : "No response details";
	}
}
