package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NewOfferDto {

	Long auctionId;
	String lenderUserName;
	double amount;
	double rate;
	boolean allowAmountSplit;
}
