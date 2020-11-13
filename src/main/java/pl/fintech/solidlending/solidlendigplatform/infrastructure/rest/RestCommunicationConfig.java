package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


@Configuration
@EnableFeignClients(clients = {HltechBankApiFeignClient.class})
@Import({
		FeignAutoConfiguration.class,
		HttpClientConfiguration.class,
		HttpMessageConvertersAutoConfiguration.class,
		PropertySourcesPlaceholderConfigurer.class
})
public class RestCommunicationConfig {
	
	String user;
	String password;
	RestCommunicationConfig(@Value("${bank.api.password}") String password,
							@Value("${bank.api.user}") String user){
		this.user = user;
		this.password = password;
	}
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
	
	@Bean
	public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
		return new BasicAuthRequestInterceptor(user, password);
	}
	
	@Bean
	public ErrorDecoder errorDecoder(){
		return new FeignExceptionHandler();
	}
	
}
