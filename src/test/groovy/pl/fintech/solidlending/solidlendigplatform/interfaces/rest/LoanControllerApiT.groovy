package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.AddMockedServiceToContext
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.AddStubRepositoriesToContext
import spock.genesis.Gen
import spock.lang.Specification

@SpringBootTest(classes = [
		LoanController,
		AddMockedServiceToContext,
		DispatcherServletAutoConfiguration,
		ServletWebServerFactoryAutoConfiguration],
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebMvc
class LoanControllerApiT extends Specification {

	@LocalServerPort
	int serverPort
	@Autowired
	LoanApplicationService loanServiceMock

	RequestSpecification restClient

	def setup() {
		restClient = RestAssured.given()
				.port(serverPort)
				.accept(MediaType.APPLICATION_JSON.toString())
				.contentType(MediaType.APPLICATION_JSON.toString())
				.log().all()
	}

	def "GET /api/loans/{loanId}/activate should call loanApplicationService with proper id"(){
		given:
			def randID = Gen.long.first()
		when:
			def response = restClient.when().get("/api/loans/"+randID+"/activate")
		then:
			response.statusCode() == 201
		and:
			1*loanServiceMock.activateLoan(randID)
	}

}
