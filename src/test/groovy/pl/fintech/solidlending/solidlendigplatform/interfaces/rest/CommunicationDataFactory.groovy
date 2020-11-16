package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import org.json.JSONObject

class CommunicationDataFactory {

	public static JSONObject createAuctionRequestBody(){
		new JSONObject("{" +
				"\"amount\": 539," +
				"\"borrower\": \"Bilbo_Baggins\"," +
				"\"loanDuration\": 9," +
				"\"loanStartDate\": \"2020-11-21\"," +
				"\"rate\": 4," +
				"\"startAuctionDate\": \"2020-11-15\"," +
				"\"auctionDuration\": 7" +
				"}")
	}

}
