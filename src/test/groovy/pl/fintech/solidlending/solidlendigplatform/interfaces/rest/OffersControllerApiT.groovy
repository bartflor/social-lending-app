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
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.AddMockedServiceToContext
import spock.genesis.Gen
import spock.lang.Specification

@SpringBootTest(classes = [
		OffersController,
		AddMockedServiceToContext,
		DispatcherServletAutoConfiguration,
		ServletWebServerFactoryAutoConfiguration],
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebMvc

class OffersControllerApiT extends Specification {

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
			def auctionId = Gen.integer.first()
			def lenderName = Gen.string(CommunicationHelper.jsonAllowedString).first()
			def amount = Gen.integer.first()
			def rate = Gen.integer.first()
		when:
			def response = restClient.given()
					.body(CommunicationHelper.createNewOfferRequest(auctionId, lenderName,	amount,	rate))
					.post("/api/offers")
		then:
			1* auctionAppSvcMock.addOffer(auctionId, lenderName, amount, rate)
		and:
			response.statusCode() == 201
	}

	def "DELETE /api/offers/{id} should call auctionApplicationService with proper parameters and \
		 return 204 status"(){
		given:
			def randID = Gen.integer.first()
		when:
			def response = restClient.delete("/api/offers/"+randID)
		then:
			1*auctionAppSvcMock.deleteOffer(_) >> {arg -> arg == randID}
		and:
			response.statusCode() == 204
	}
}
