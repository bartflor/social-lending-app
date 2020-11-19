package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionDomainService
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.AddMockedServiceToContext
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.AddStubRepositoriesToContext
import spock.genesis.Gen
import spock.lang.Specification

@Import(AddMockedServiceToContext)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OffersControllerTest extends Specification {

	@LocalServerPort
	int port

	@Autowired
	AuctionApplicationService auctionAppSvcMock

	RequestSpecification restClient

	def setup(){
		restClient = RestAssured.given()
				.port(port)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.log().all()
	}

	def "POST: /api/offers should call auctionDomainService with valid parameters"() {
		given:
			def auctionId = Gen.long.first()
			def lenderName = Gen.string(CommunicationDataFactory.jsonAllowedString).first()
			def amount = Gen.integer.first()
			def rate = Gen.integer.first()
			def allowSplit = Gen.long.first() > 0 //random boolean
		when:
			def response = restClient.given()
					.body(CommunicationDataFactory.createNewOfferRequest(auctionId, lenderName,	amount,	rate, allowSplit))
					.post("/api/offers/")
		then:
			1* auctionAppSvcMock.addOffer(auctionId, lenderName, amount, rate, allowSplit)
		and:
			response.statusCode() == 201

	}
}
