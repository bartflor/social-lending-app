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
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.auction.OffersSelectionPolicy
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanTestHelper
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.AddMockedServiceToContext
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
	@Autowired
	OffersSelectionPolicy policyMock


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
			def loan = LoanTestHelper.crateLoan(randID)
			def dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
					.withZone(ZoneId.systemDefault())

		when:
			def response = restClient.when().get("/api/auctions/"+randID+"/create-loan")
		then:
			1*auctionServiceMock.createLoanFromEndingAuction(randID, policyMock) >> randID
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

	def "POST /api/auctions should call auctionApplicationService with proper parameters and \
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

	def "DELETE /api/auctions/{id} should call auctionApplicationService with proper parameters and \
		 return 204 response status"(){
		given:
			def randID = Gen.integer.first()

		when:
			def response = restClient.delete("/api/auctions/"+randID)
		then:
			1*auctionServiceMock.deleteAuction(_) >> {arg -> arg == randID}
		and:
			response.statusCode() == 204
	}

}
