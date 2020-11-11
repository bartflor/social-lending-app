package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest

import org.json.JSONObject

class ApiResponseFactory {

	static String notEnoughBalanceResponse() {
		new JSONObject("{\"code\": 404," +
				" \"message\": \"Unprocessable\"," +
				" \"details\": [\"Transaction rejected - the source account does not have sufficient cash\"]" +
				"}")
	}

	static String accountDetailsResponse(String number, double balance){
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

	static String transactionRequestBody(String source, String target, double amount){
		"{\n" +
				"  \"sourceAccountNumber\": \""+source+"\",\n" +
				"  \"targetAccountNumber\": \""+target+"\",\n" +
				"  \"amount\": "+amount+"\n" +
				"}"
	}
	static TransactionRequest createTransactionsDto(String source, String target, double amount) {
		new TransactionRequest(source, target, amount)
	}

	static String paymentRequest(String number, double amount) {
		"{\n" +
		"\"accountNumber\": \""+number+"\",\n" +
		"\"amount\": "+amount+
		"\n}"
	}
}
