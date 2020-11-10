package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
public class RestResponseErrorHandler extends DefaultResponseErrorHandler {
  
  @Override
  public void handleError(ClientHttpResponse httpResponse) throws IOException {

    if (httpResponse.getStatusCode().is5xxServerError()) {
      // handle SERVER_ERROR
    } else if (httpResponse.getStatusCode().is4xxClientError()) {
      // handle CLIENT_ERROR
      // handle no enough cash?
    }
  }
}
