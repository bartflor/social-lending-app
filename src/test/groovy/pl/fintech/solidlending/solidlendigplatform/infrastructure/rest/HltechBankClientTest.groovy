package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest


import org.springframework.http.ResponseEntity
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class HltechBankClientTest extends Specification {

	def apiClientMock = Mock(HltechBankApiFeignClient)
	@Subject
	def hltechBankClient = new HltechBankClient(apiClientMock)

	def "Transfer should return transaction number when money transfer success"() {
		given:
			def sourceAccount = UUID.randomUUID().toString()
			def targetAccount = UUID.randomUUID().toString()
			def transactionNumber = UUID.randomUUID().toString()
			def amount = Gen.double.first()
			def transactionRequest = TransactionRequest.builder()
					.amount(amount)
					.sourceAccountNumber(sourceAccount)
					.targetAccountNumber(targetAccount)
					.build()
		when:
			def result = hltechBankClient.transfer(sourceAccount, targetAccount, amount)
		then:
			apiClientMock.transfer(transactionRequest) >>
					ResponseEntity.created(new URI("/transactions/"+transactionNumber)).build()
		and:
			result == transactionNumber

	}

	def "CreateAccount"() {
	}

	def "Payment"() {
	}

	def "GetAccountDetails"() {
	}
}
