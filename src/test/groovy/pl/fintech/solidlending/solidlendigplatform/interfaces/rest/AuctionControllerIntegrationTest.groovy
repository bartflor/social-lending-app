package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionDomainFactory
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository
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

	def "GET /api/auctions/{auctionId}/create-loan should create new loan in repository"() {
		given:
			def amount = Gen.integer(0, Integer.MAX_VALUE).first()
			def auction = AuctionDomainFactory.createAuctionWithAmount(amount)
			def id = auctionRepository.save(auction)
			auction.addNewOffer(AuctionDomainFactory.createOfferWithAmount(amount,id))
			auctionRepository.updateAuction(id, auction)

		when:
			def response = restClient.when().get("/api/auctions/" + id + "/create-loan")
		then:
			response.statusCode() == 201
		and:
			loanRepository.findAll().size() == 1
	}


}
