package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.ExternalTransferOrderEvent
import pl.fintech.solidlending.solidlendigplatform.domain.common.UserService
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.payment.PaymentService
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.AddMockedServiceToContext
import spock.genesis.Gen
import spock.lang.Specification

import static org.hamcrest.Matchers.equalTo


@SpringBootTest(classes = [
		AccountController,
		AddMockedServiceToContext,
		DispatcherServletAutoConfiguration,
		ServletWebServerFactoryAutoConfiguration],
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebMvc
class AccountControllerTest extends Specification {


	@LocalServerPort
	int serverPort
	@Autowired
	UserService userServiceMock
	@Autowired
	PaymentService paymentServiceMock

	RequestSpecification restClient

	def setup(){
		restClient = RestAssured.given()
			.port(serverPort)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.log().all()
	}

	def "GET /api/accounts/{userName} should response with user details given user name"() {
		given:
			def userName = Gen.string(CommunicationDataFactory.jsonAllowedString).first()
			def balance = Gen.integer.first()
			def name = Gen.string(CommunicationDataFactory.jsonAllowedString).first()
			def surname = Gen.string(CommunicationDataFactory.jsonAllowedString).first()
			def phoneNumber = Gen.string(CommunicationDataFactory.jsonAllowedString).first()
			def email = Gen.string(CommunicationDataFactory.jsonAllowedString).first()
			def userDetails = CommunicationDataFactory.getUserDetailsFrom(userName, name, surname, phoneNumber, email)
			def serviceResponse = new Money(balance)
		when:
			def response = restClient.when().get("/api/accounts/"+userName)
		then:
			1*userServiceMock.getUserDetails(userName) >> userDetails
			1*paymentServiceMock.checkUserBalance(userName) >> serviceResponse
		and:
			response.statusCode() == 200
			response.then().assertThat()
					.body("userName", equalTo(userName))
					.body("accountBalance", equalTo(balance))
					.body("name", equalTo(name))
					.body("surname", equalTo(surname))
					.body("phoneNumber", equalTo(phoneNumber))
					.body("email", equalTo(email))
	}

	def "POST /api/account/deposit should call paymentService.depositMoneyIntoPlatform \
		 with params from request body and return 201 response status"() {
		given:
			def userName = Gen.string(CommunicationDataFactory.jsonAllowedString).first()
			def amount = Gen.integer.first()
			def transferDto = CommunicationDataFactory.createTransferDto(amount, userName)
			def event = transferDto.createTransferOrderEvent(ExternalTransferOrderEvent.TransferType.DEPOSIT)
		when:
			def response = restClient.body(CommunicationDataFactory.createTransferRequest(userName, amount))
					.post("api/accounts/deposit")

		then:
			1*paymentServiceMock.executeExternal(event)
		and:
			response.statusCode() == 201


	}

	def "POST /api/account/withdrawal should call paymentService.withdrawMoneyFromPlatform\
		 with params from request body and return 201 response status"() {
		given:
			def userName = Gen.string(CommunicationDataFactory.jsonAllowedString).first()
			def amount = Gen.integer.first()
			def transferDto = CommunicationDataFactory.createTransferDto(amount, userName)
			def event = transferDto.createTransferOrderEvent(ExternalTransferOrderEvent.TransferType.WITHDRAWAL)
		when:
			def response = restClient.body(CommunicationDataFactory.createTransferRequest(userName, amount))
					.post("api/accounts/withdrawal")

		then:
			1*paymentServiceMock.executeExternal(event)
		and:
			response.statusCode() == 201
	}

}
