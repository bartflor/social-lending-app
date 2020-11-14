package pl.fintech.solidlending.solidlendigplatform.domain.payment

import org.mockito.Mock
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Lender
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.LenderRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class TransferServiceTest extends Specification {
	def bankClientMock = Mock(BankClient)
	def lenderRepoMock = Mock(LenderRepository)
	def borrowerRepoMock = Mock(BorrowerRepository)


	@Subject
	def transferService = new TransferService(bankClientMock, lenderRepoMock, borrowerRepoMock)

	def "MakeInternalTransfer should find users and return BankClient transfer reference id, given users name"() {
		given:
			def sourceUserAccount = UUID.randomUUID().toString()
			def targetUserAccount = UUID.randomUUID().toString()
			def refId = UUID.randomUUID().toString()
			def amount= Gen.integer(0, 10000).first()
			def sourceUserName = Gen.string(5,20).first()
			def targetUserName = Gen.string(5,20).first()
			def sourceUser = createLender(sourceUserAccount, sourceUserName)
			def targetUser = createBorrower(targetUserAccount, targetUserName)
		when:
			def result = transferService.makeInternalTransfer(sourceUserName, targetUserName, amount)
		then:
			1 * bankClientMock.transfer(sourceUserAccount, targetUserAccount, amount) >> refId
			1 * borrowerRepoMock.findBorrowerByUserName(targetUserName) >> Optional.of(targetUser)
			1 * borrowerRepoMock.findBorrowerByUserName(sourceUserName) >> Optional.empty()
			1 * lenderRepoMock.findLenderByUserName(sourceUserName) >> Optional.of(sourceUser)
			1 * lenderRepoMock.findLenderByUserName(targetUserName) >> Optional.empty()

		and:
			result == refId
	}

	def "MakeInternalTransfer should throw exception, when targetUser not found with given users name"() {
		given:
			def targetUserAccount = UUID.randomUUID().toString()
			def refId = UUID.randomUUID().toString()
			def amount= Gen.integer(0, 10000).first()
			def sourceUserName = Gen.string(5,20).first()
			def targetUserName = Gen.string(5,20).first()

			def sourceUser = createBorrower(targetUserAccount, targetUserName)
		when:
			transferService.makeInternalTransfer(sourceUserName, targetUserName, amount)
		then:
			0 * bankClientMock.transfer(_, _, _) >> refId
			1 * borrowerRepoMock.findBorrowerByUserName(sourceUserName) >> Optional.empty()
			1 * lenderRepoMock.findLenderByUserName(sourceUserName) >> Optional.of(sourceUser)
			1 * lenderRepoMock.findLenderByUserName(targetUserName) >> Optional.empty()
			1 * borrowerRepoMock.findBorrowerByUserName(targetUserName) >> Optional.empty()
		and:
			def exception = thrown(UserNotFoundException)
			exception.getMessage() == "User with username:"+targetUserName+" not found."
	}

	def "MakeInternalTransfer should throw exception, when sourceUser not found with given users name"() {
		given:
			def refId = UUID.randomUUID().toString()
			def amount= Gen.integer(0, 10000).first()
			def sourceUserName = Gen.string(5,20).first()
			def targetUserName = Gen.string(5,20).first()

		when:
			transferService.makeInternalTransfer(sourceUserName, targetUserName, amount)
		then:
			0 * bankClientMock.transfer(_, _, amount) >> refId
			1 * borrowerRepoMock.findBorrowerByUserName(sourceUserName) >> Optional.empty()
			1 * lenderRepoMock.findLenderByUserName(sourceUserName) >> Optional.empty()

		and:
			def exception = thrown(UserNotFoundException)
			exception.getMessage() == "User with username:"+sourceUserName+" not found."
	}

	private static Borrower createBorrower(String targetUserAccount, String targetUserName) {
		Borrower.builder()
				.userDetails(UserDetails.builder()
						.accountNumber(targetUserAccount)
						.name(Gen.string(20).first())
						.email(Gen.string(20).first())
						.userName(targetUserName)
						.build())
				.build()
	}

	private static Lender createLender(String sourceUserAccount, String sourceUserName) {
		Lender.builder()
				.userDetails(UserDetails.builder()
						.accountNumber(sourceUserAccount)
						.name(Gen.string(20).first())
						.email(Gen.string(20).first())
						.userName(sourceUserName)
						.build())
				.build()
	}
}


