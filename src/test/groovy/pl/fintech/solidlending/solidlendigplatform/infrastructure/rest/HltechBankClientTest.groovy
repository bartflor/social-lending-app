package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

class HltechBankClientTest extends Specification {
	@Subject
	HltechBankClient bankClient = new HltechBankClient(new RestTemplateBuilder(), "example", "example",
	new RestResponseErrorHandler());

	MockRestServiceServer server = MockRestServiceServer.bindTo(bankClient.template).build();

	def "transaction should call bank api and not throw exception getting 201 status response"(){
		given:
		server.expect(requestTo("https://hltechbank.thebe-team.sit.fintechchallenge.pl/transactions"))
				.andRespond(withStatus(HttpStatus.CREATED))
		expect:
			bankClient.transfer(UUID.randomUUID().toString(),
					UUID.randomUUID().toString() ,
					Gen.integer.first())
	}

	def "getAccountDetails should call api and return account details"(){
		given:
			def response = new JSONObject(
					"{\"name\": \"SLP_USR testLender\"," +
					"\"number\": \"e0c30b15-02e1-423f-9fa3-2a9cf411980d\"," +
					"\"transactions\": [ " +
							"{ " +
							"\"id\": 64, " +
							"\"type\": \"CREDIT\", " +
							"\"amount\": 1.00, " +
							"\"referenceId\": \"7c0b9fd5-c94f-44ea-a39a-fb2abeabc129\", " +
							"\"timestamp\": \"2020-11-10T08:39:25.759583Z\" " +
							"}," +
							"{ " +
							"\"id\": 27, " +
							"\"type\": \"DEBIT\", " +
							"\"amount\": 100.00, " +
							"\"referenceId\": \"5c396871-1513-44c7-bd60-48a52850f19f\", " +
							"\"timestamp\": \"2020-11-08T19:21:33.233723Z\" " +
							"}], " +
					"\"accountBalance\": 107.00}")
			server.expect(requestTo("https://hltechbank.thebe-team.sit.fintechchallenge.pl/accounts/e0c30b15-02e1-423f-9fa3-2a9cf411980d"))
					.andRespond(withSuccess(response.toString(), MediaType.APPLICATION_JSON))
		when:
			def resp = bankClient.getDetails("e0c30b15-02e1-423f-9fa3-2a9cf411980d")
		then:
			resp
	}
}
