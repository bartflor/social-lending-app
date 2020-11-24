package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import io.restassured.RestAssured
import io.restassured.http.ContentType
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
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService

import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationServiceImpl
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionDomainServiceImpl
import pl.fintech.solidlending.solidlendigplatform.domain.auction.BestOffersRatePolicy
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanDomainFactory
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.AddMockedServiceToContext
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.AddStubRepositoriesToContext
import spock.genesis.Gen
import spock.lang.Specification

import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import static org.hamcrest.Matchers.equalTo

@SpringBootTest(classes = [
		AuctionsController,
		AddMockedServiceToContext,
		DispatcherServletAutoConfiguration,
		ServletWebServerFactoryAutoConfiguration],
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebMvc

class AuctionsControllerTest extends Specification {

	@LocalServerPort
	int serverPort
	@Autowired
	LoanApplicationService loanServiceMock
	@Autowired
	AuctionApplicationService auctionServiceMock

	RequestSpecification restClient

	def setup() {
		restClient = RestAssured.given()
				.port(serverPort)
				.accept(MediaType.APPLICATION_JSON.toString())
				.contentType(MediaType.APPLICATION_JSON.toString())
				.log().all()
	}

	def "GET /api/auctions/{auctionId}/create-loan should return created loanDto"(){
		given:
			def randID = Gen.integer.first()
			def loan = LoanDomainFactory.crateLoan(randID)
			def dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
					.withZone(ZoneId.systemDefault())

		when:
			def response = restClient.when().get("/api/auctions/"+randID+"/create-loan")
		then:
			1*auctionServiceMock.createLoanFromEndingAuction(randID, new BestOffersRatePolicy()) >> randID
			1*loanServiceMock.findLoanById(randID) >> loan
		and:
			response.statusCode() == 201
			response.then()
					.assertThat()
					.body("borrowerUserName", equalTo(loan.getBorrowerUserName()))
					.body("status", equalTo(loan.getStatus().toString()))
					.body("rate", equalTo(loan.getAverageRate().getPercentValue().floatValue()))
					.body("startDate", equalTo(dateFormatter.format(loan.getStartDate())))
	}

	def "POST /api/auctions call should call auctionApplicationService with proper parameters and \
		 return 201 status given proper request"(){
		given:
			def randID = Gen.integer.first()
			def borrowerName = Gen.string(CommunicationDataFactory.jsonAllowedString).first()
			def auctionDuration = Gen.integer.first()
			def amount = Gen.integer.first()
			def rate = Gen.integer.first()
			def loanDuration = Gen.integer.first()

		when:
			def response = restClient.given()
					.contentType(ContentType.JSON)
					.body(CommunicationDataFactory.createAuctionRequestBody(borrowerName, auctionDuration,
							amount, rate, loanDuration))
					.post("/api/auctions")
		then:
			1*auctionServiceMock.createNewAuction(borrowerName, Period.ofDays(auctionDuration),
					amount, Period.ofMonths(loanDuration), rate) >> randID
		and:
			response.statusCode() == 201
	}



}
