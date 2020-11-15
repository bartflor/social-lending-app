package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.json.JSONObject;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.AccountNotFoundException;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.UnprocessableRequestException;
@Log
@AllArgsConstructor
public class FeignExceptionHandler implements ErrorDecoder {
	
	@Override
	public Exception decode(String exceptionSource, Response response) {
		
		if(response.status() == 422){
      		String messageDetails = getResponseBodyField(response, "details");
			log.severe("Unprocessable bank API request: "+messageDetails+
					"\n Exception at: "+exceptionSource);
			throw new UnprocessableRequestException(messageDetails);
		}
		if (response.status() == 404) {
			String messageDetails = getResponseBodyField(response, "details");
			log.severe("NotFound exception for bank API request: "+messageDetails+
					"\n Exception at: "+exceptionSource);
			return new AccountNotFoundException(messageDetails);
		}
		log.severe("Unknown exception occur during communication with bank API: "+response+
			"\n Exception at: "+exceptionSource);
		return new RuntimeException(response.toString());
	}
	
	private String getResponseBodyField(Response response, String field) {
		JSONObject responseBody = new JSONObject();
		if(response.body() != null){
			responseBody = new JSONObject(response.body().toString());
		}
		return responseBody.has(field) ? responseBody.getString(field) : "No response details";
	}
}
