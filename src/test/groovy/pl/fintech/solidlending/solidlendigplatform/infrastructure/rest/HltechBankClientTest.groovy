package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest


import org.springframework.http.ResponseEntity
import pl.fintech.solidlending.solidlendigplatform.domain.payment.TransactionDetails
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.BankCommunicationFailedException
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class HltechBankClientTest extends Specification {

	def apiClientMock = Mock(HltechBankApiFeignClient)
	@Subject
	def hltechBankClient = new HltechBankClient(apiClientMock)

	def "Transfer should return transaction number when money transfer success"() {
		given:
			def sourceAccount = UUID.randomUUID()
			def targetAccount = UUID.randomUUID()
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
			1 * apiClientMock.transfer(transactionRequest) >>
					ResponseEntity.created(new URI("/transactions/"+transactionNumber)).build()
		and:
			result == transactionNumber

	}

	def "GetAccountBalance should check if api response is valid and\
		return account balance"() {
		given:
			def accountNumber = UUID.randomUUID()
			def amount = BigDecimal.valueOf(Gen.double.first())
			def response = ResponseEntity.ok(RestInfrastructureFactory.createAccountDetailsDto(accountNumber, amount))
		when:
			def result = hltechBankClient.getAccountBalance(accountNumber)
		then:
			1* apiClientMock.accountDetails(accountNumber) >> response
		and:
			result == amount

	}

	def "getAccountTransactions should check if api response is valid and\
		return list of transactions from response"(){
		given:
			def accountNumber = UUID.randomUUID()
			def refId1 = UUID.randomUUID()
			def amount1 = BigDecimal.valueOf(Gen.double.first())
			def transaction1 = RestInfrastructureFactory.createTransactionDto(amount1, refId1)
			def refId2 = UUID.randomUUID()
			def amount2 = BigDecimal.valueOf(Gen.double.first())
			def transaction2 = RestInfrastructureFactory.createTransactionDto(amount2, refId2)
			def response = ResponseEntity.ok(RestInfrastructureFactory.createAccountDetailsDtoWithTransactions(
												accountNumber,
												BigDecimal.valueOf(Gen.double.first()),
												List.of(transaction1, transaction2)))
		when:
			def result = hltechBankClient.getAccountTransactions(accountNumber)
		then:
			1* apiClientMock.accountDetails(accountNumber) >> response
		and:
			result.size() == 2
			result.contains(TransactionDetails.builder().referenceId(refId1).amount(amount1).type(TransactionDetails.TransactionType.CREDIT).build())
			result.contains(TransactionDetails.builder().referenceId(refId2).amount(amount2).type(TransactionDetails.TransactionType.CREDIT).build())
	}

	def "CreateAccount should throw exception getting response with error status code"(){
		given:
			def userName = Gen.string(20).first()
			def response = ResponseEntity.badRequest().build()
		when:
			hltechBankClient.createAccount(userName)
		then:
			1 * apiClientMock.createAccount(userName) >> response
		and:
			thrown BankCommunicationFailedException
	}

	def "Payment should throw exception getting response with error status code"(){
		given:
			def accountNumber = UUID.randomUUID()
			def amount = BigDecimal.valueOf(Gen.double.first())
			def response = ResponseEntity.badRequest().build()
			def paymentRequest = RestInfrastructureFactory.createPaymentRequest(accountNumber, amount)
		when:
			hltechBankClient.payment(accountNumber, amount)
		then:
			1 * apiClientMock.payment(paymentRequest) >> response
		and:
			thrown BankCommunicationFailedException
	}
}
