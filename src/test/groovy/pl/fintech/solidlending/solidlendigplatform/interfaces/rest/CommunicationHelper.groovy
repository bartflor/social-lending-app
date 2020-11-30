package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import org.json.JSONObject
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails

import java.util.regex.Pattern

class CommunicationHelper {
	public static final Pattern jsonAllowedString = Pattern.compile("[a-zA-Z]{2,20}+")

	static String createAuctionRequestBody(String borrowerName, int auctionDuration, int amount,
	                                           int rate, int loanDuration){
		new JSONObject("{" +
				"\"amount\": "+amount+"," +
				"\"borrower\": \""+borrowerName+"\"," +
				"\"loanDuration\": "+loanDuration+"," +
				"\"rate\": "+rate+"," +
				"\"auctionDuration\": "+auctionDuration+
				"}")
	}

	static String createNewOfferRequest(long auctionId, String lenderName,	int amount,	int rate) {
		new JSONObject("{ " +
				"\"amount\": "+amount+"," +
				"\"auctionId\": "+auctionId+"," +
				"\"lenderUserName\": \""+lenderName+"\"," +
				"\"rate\": "+rate+
				"}")
	}

	static UserDetails getUserDetailsFrom(String userName, String name, String surname, String phoneNumber, String email){
		UserDetails.builder()
				.userName(userName)
				.privateAccountNumber(UUID.randomUUID())
				.platformAccountNumber(UUID.randomUUID())
				.phoneNumber(phoneNumber)
				.email(email)
				.name(name)
				.surname(surname)
				.build()
	}

	static String createTransferRequest(String userName, int amount) {
		new JSONObject("{ " +
				"\"amount\": "+amount+"," +
				"\"userName\": \""+userName+"\""+
				"}")
	}

	static TransferDto createTransferDto(int amount, String userName) {
		def transferDto = new TransferDto()
		transferDto.setAmount(amount)
		transferDto.setUserName(userName)
		transferDto
	}
}
