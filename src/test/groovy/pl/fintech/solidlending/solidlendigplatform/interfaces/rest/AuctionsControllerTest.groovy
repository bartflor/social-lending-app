package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.auction.BestOfferRatePolicy
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanDomainFactory
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.AddMockedServiceToContext
import spock.genesis.Gen
import spock.lang.Specification

import java.time.ZoneId
import java.time.format.DateTimeFormatter

import static org.hamcrest.Matchers.equalTo

@Import(AddMockedServiceToContext)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

			auctionServiceMock.createLoanFromEndingAuction(randID, new BestOfferRatePolicy()) >> randID
			loanServiceMock.findLoanById(randID) >> loan
			def dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
					.withZone(ZoneId.systemDefault())

		when:
			def response = restClient.when().get("/api/auctions/"+randID+"/create-loan")
		then:
			response.statusCode() == 201
		response.toString()
		and:
			response.then()
					.assertThat()
					.body("borrowerUserName", equalTo(loan.getBorrowerUserName()))
					.body("status", equalTo(loan.getStatus().toString()))
					.body("rate", equalTo(loan.getAverageRate().getPercentValue().floatValue()))
					.body("startDate", equalTo(dateFormatter.format(loan.getStartDate())))
	}

	def "POST /api/auctions should return 201 status, create new auction and call repository save given proper request"(){
		given:
			def randID = Gen.integer.first()
			def loan = LoanDomainFactory.crateLoan(randID)

			auctionServiceMock.createLoanFromEndingAuction(randID, new BestOfferRatePolicy()) >> randID
			loanServiceMock.findLoanById(randID) >> loan

		when:
			def response = restClient.given()
					.contentType(ContentType.JSON)
					.body(CommunicationDataFactory.createAuctionRequestBody().toString())
					.post("/api/auctions")
		then:
			response.statusCode() == 201
			response.toString()
		and:
			response
	}



}
