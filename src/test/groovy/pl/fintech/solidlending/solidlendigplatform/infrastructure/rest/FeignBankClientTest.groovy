package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.UnprocessableRequestException
import spock.genesis.Gen
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

@ContextConfiguration(
		classes = [RestCommunicationConfig],
		initializers = [WireMockInitializer]
)
class FeignBankClientTest extends Specification {

	@Autowired
	HltechBankApiFeignClient feign
	@Autowired
	WireMockServer wireMockServer

	def "Transfer should call api POST: /transactions with given transaction details \
		and return location header when response status 201"() {
		given:
			def sourceAccount = UUID.randomUUID().toString()
			def targetAccount = UUID.randomUUID().toString()
			def amount = Gen.double.first()
			def dto = RestInfrastructureFactory.createTransactionsDto(sourceAccount,targetAccount,amount)
			wireMockServer.stubFor(post("/transactions")
					.withBasicAuth("usr", "pass")
					.withRequestBody(equalToJson(RestInfrastructureFactory.transactionRequestBody(sourceAccount,targetAccount,amount)))
					.willReturn(aResponse().withStatus(201)
							.withHeader("Location",
									"transactions/76def15f-1dd8-4b8a-a4ec-45b951fddc90")
					))

		expect:
			def resp = feign.transfer(dto)
			resp.headers.get("Location").first() ==  "transactions/76def15f-1dd8-4b8a-a4ec-45b951fddc90"
	}



	def "Transfer should call api POST: /transactions with given transactionDetails \
		and throw exception, given response with status 422"() {
		given:
			def dto = RestInfrastructureFactory.createTransactionsDto(UUID.randomUUID().toString(),
					UUID.randomUUID().toString(), Gen.double.first())
			wireMockServer.stubFor(post("/transactions")
					.withBasicAuth("usr", "pass")
					.willReturn(aResponse().withStatus(HttpStatus.UNPROCESSABLE_ENTITY.value())
					.withBody(RestInfrastructureFactory.notEnoughBalanceResponse())))

		when:
			feign.transfer(dto)
		then:
			thrown(UnprocessableRequestException)
	}

	def "accountDetails should call api POST:/accounts/{accountNumber} \
		and return proper accountDetailsDto"(){
		given:
			def accountNumber = UUID.randomUUID()
			def balance = Gen.double.first()
			wireMockServer.stubFor(get("/accounts/"+accountNumber)
					.withBasicAuth("usr", "pass")
					.willReturn(aResponse()
							.withStatus(HttpStatus.OK.value())
							.withBody(RestInfrastructureFactory.accountDetailsResponse(accountNumber, balance))
							.withHeader("Content-Type", "application/json")))
		when:
			def resp = feign.accountDetails(accountNumber).getBody()
		then:
			resp.accountBalance == balance
			resp.number == accountNumber
	}

	def "payment should call api POST: /payment with given paymentDetails \
		and return 201 response status"(){
		given:
			def accountNumber = UUID.randomUUID().toString()
			def amount = Gen.double.first()
			wireMockServer.stubFor(post("/payments")
					.withBasicAuth("usr","pass")
					.withRequestBody(equalToJson(RestInfrastructureFactory.paymentRequestJson(accountNumber, amount)))
					.willReturn(aResponse()
							.withStatus(HttpStatus.CREATED.value())))
		when:
			def resp = feign.payment(new PaymentRequest(accountNumber, amount))
		then:
			resp.getStatusCode() == HttpStatus.CREATED

	}

}
