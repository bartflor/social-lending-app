package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.auction.BestOfferRatePolicy
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanDomainFactory
import spock.genesis.Gen
import spock.lang.Specification

import java.time.Period

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
		when:
			def response = restClient.when().get("/api/auctions/"+randID+"/create-loan")
		then:
			response.statusCode() == 201
		and:
			response.body().as(LoanDto) == LoanDto.from(loan)
	}


}
