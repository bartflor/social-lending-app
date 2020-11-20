package pl.fintech.solidlending.solidlendigplatform.domain.payment

import pl.fintech.solidlending.solidlendigplatform.domain.common.TransferOrderEvent
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Lender
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.User
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import spock.genesis.Gen

class PaymentDomainFactory {

	static createTransferOrderEvent(String sourceUserName, String targetUserName, Money amount){
		TransferOrderEvent.builder()
			.sourceUserName(sourceUserName)
			.targetUserName(targetUserName)
			.amount(amount)
			.build()
	}

	static Borrower createBorrower(UUID targetUserAccount, String targetUserName) {
		Borrower.builder()
				.userDetails(UserDetails.builder()
						.platformAccountNumber(targetUserAccount)
						.name(Gen.string(20).first())
						.email(Gen.string(20).first())
						.userName(targetUserName)
						.build())
				.build()
	}

	static Lender createLender(UUID sourceUserAccount, String sourceUserName) {
		Lender.builder()
				.userDetails(UserDetails.builder()
						.platformAccountNumber(sourceUserAccount)
						.name(Gen.string(20).first())
						.email(Gen.string(20).first())
						.userName(sourceUserName)
						.build())
				.build()
	}

	static User createUser(UUID platformAccount, UUID privateAccount) {
		Lender.builder()
				.userDetails(UserDetails.builder()
						.platformAccountNumber(platformAccount)
						.privateAccountNumber(privateAccount)
						.build())
				.build()
	}
}
