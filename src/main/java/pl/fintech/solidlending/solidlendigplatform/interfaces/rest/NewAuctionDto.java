package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NewAuctionDto {
	long auctionId;
	String borrower;
	int auctionDuration;
	int loanDuration;
	double amount;
	double rate;
}
