package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionDomainFactory
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository
import pl.fintech.solidlending.solidlendigplatform.domain.auction.BestOfferRatePolicy
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanDomainFactory
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRepository
import spock.genesis.Gen
import spock.lang.Specification

@Import(AddStubRepositoriesToContext)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuctionControllerIntegrationTest extends Specification {

	@LocalServerPort
	int serverPort
	@Autowired
	AuctionRepository auctionRepository
	@Autowired
	LoanRepository loanRepository

	RequestSpecification restClient

	def setup() {
		restClient = RestAssured.given()
				.port(serverPort)
				.accept(MediaType.APPLICATION_JSON.toString())
				.contentType(MediaType.APPLICATION_JSON.toString())
				.log().all()
	}

	def "GET /auctions/{auctionId}/create-loan should create new loan in repository"() {
		given:
			def auction = AuctionDomainFactory.createAuction()
			def id = auctionRepository.save(auction)

		when:
			def response = restClient.when().get("/auctions/" + id + "/create-loan")
		then:
			response.statusCode() == 201
		and:
			loanRepository.findAll().size() == 1
	}


}
