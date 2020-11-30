package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest

import org.json.JSONObject
import spock.genesis.Gen

import java.time.Instant
import java.time.LocalDate
import java.time.temporal.TemporalAccessor

class RestCommunicationHelper {

	static String notEnoughBalanceResponse() {
		new JSONObject("{\"code\": 404," +
				" \"message\": \"Unprocessable\"," +
				" \"details\": [\"Transaction rejected - the source account does not have sufficient cash\"]" +
				"}")
	}

	static String accountDetailsResponse(UUID number, double balance){
		new JSONObject(
				"{\"name\": \"SLP_USR testLender\"," +
						"\"number\": \""+number+"\"," +
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
						"\"accountBalance\": "+balance+"}")
	}

	static String transactionRequestBody(UUID source, UUID target, double amount){
		"{\n" +
				"  \"sourceAccountNumber\": \""+source+"\",\n" +
				"  \"targetAccountNumber\": \""+target+"\",\n" +
				"  \"amount\": "+amount+"\n" +
				"}"
	}
	static TransactionRequest createTransactionsDto(UUID source, UUID target, double amount) {
		new TransactionRequest(source, target, amount)
	}

	static String paymentRequestJson(UUID number, double amount) {
		"{\n" +
		"\"accountNumber\": \""+number+"\",\n" +
		"\"amount\": "+amount+
		"\n}"
	}

	static AccountDetailsDto createAccountDetailsDto(UUID account, BigDecimal balance) {
		AccountDetailsDto.builder()
				.number(account)
				.accountBalance(balance)
				.name(Gen.string(20).first())
				.transactions(List.of(BankTransactionDto.builder()
										.id(Gen.long.first())
										.type("CREDIT")
										.amount(Gen.double.first())
										.referenceId(UUID.randomUUID())
										.timestamp(Gen.getDate().first().toInstant())
										.build()))
				.build()
	}

	static AccountDetailsDto createAccountDetailsDtoWithTransactions(UUID account,
	                                                                BigDecimal balance,
	                                                                List<BankTransactionDto> transactions) {
		AccountDetailsDto.builder()
				.number(account)
				.accountBalance(balance)
				.name(Gen.string(20).first())
				.transactions(transactions)
				.build()
	}

	static BankTransactionDto createTransactionDto(double amount, UUID referenceId) {
		BankTransactionDto.builder()
				.id(Gen.long.first())
				.type("CREDIT")
				.amount(amount)
				.referenceId(referenceId)
				.build()
	}

	static PaymentRequest createPaymentRequest(UUID accountNumber, double amount) {
		new PaymentRequest(accountNumber, amount)
	}
}
