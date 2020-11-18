package pl.fintech.solidlending.solidlendigplatform.domain.payment


import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Lender
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.LenderRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class TransferServiceTest extends Specification {
	def bankClientMock = Mock(BankClient)
	def lenderRepoMock = Mock(LenderRepository)
	def borrowerRepoMock = Mock(BorrowerRepository)


	@Subject
	def transferService = new TransferServiceImpl(bankClientMock, lenderRepoMock, borrowerRepoMock)

	def "execute() should find users and return BankClient transfer reference id, given users name"() {
		given:
			def sourceUserAccount = UUID.randomUUID().toString()
			def targetUserAccount = UUID.randomUUID().toString()
			def refId = UUID.randomUUID().toString()
			def amount = new Money(Gen.integer(0, 10000).first() as double)
			def sourceUserName = Gen.string(5,20).first()
			def targetUserName = Gen.string(5,20).first()
			def sourceUser = PaymentDomainFactory.createLender(sourceUserAccount, sourceUserName)
			def targetUser = PaymentDomainFactory.createBorrower(targetUserAccount, targetUserName)
		when:
			def result = transferService.execute(PaymentDomainFactory
					.createTransferOrderEvent(sourceUserName, targetUserName, amount))
		then:
			1 * bankClientMock.transfer(sourceUserAccount, targetUserAccount, amount.getValue().doubleValue()) >> refId
			1 * borrowerRepoMock.findBorrowerByUserName(targetUserName) >> Optional.of(targetUser)
			1 * borrowerRepoMock.findBorrowerByUserName(sourceUserName) >> Optional.empty()
			1 * lenderRepoMock.findLenderByUserName(sourceUserName) >> Optional.of(sourceUser)
			1 * lenderRepoMock.findLenderByUserName(targetUserName) >> Optional.empty()

		and:
			result == refId
	}

	def "execute() should throw exception, when targetUser not found with given users name"() {
		given:
			def targetUserAccount = UUID.randomUUID().toString()
			def refId = UUID.randomUUID().toString()
			def amount = new Money(Gen.integer(0, 10000).first() as double)
			def sourceUserName = Gen.string(5,20).first()
			def targetUserName = Gen.string(5,20).first()

			def sourceUser = PaymentDomainFactory.createBorrower(targetUserAccount, targetUserName)
		when:
			transferService.execute(PaymentDomainFactory.createTransferOrderEvent(sourceUserName, targetUserName, amount))
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

	def "execute() should throw exception, when sourceUser not found with given users name"() {
		given:
			def refId = UUID.randomUUID().toString()
			def amount = new Money(Gen.integer(0, 10000).first() as double)
			def sourceUserName = Gen.string(5,20).first()
			def targetUserName = Gen.string(5,20).first()

		when:
			transferService.execute(PaymentDomainFactory.createTransferOrderEvent(sourceUserName, targetUserName, amount))
		then:
			0 * bankClientMock.transfer(_, _, amount) >> refId
			1 * borrowerRepoMock.findBorrowerByUserName(sourceUserName) >> Optional.empty()
			1 * lenderRepoMock.findLenderByUserName(sourceUserName) >> Optional.empty()

		and:
			def exception = thrown(UserNotFoundException)
			exception.getMessage() == "User with username:"+sourceUserName+" not found."
	}


}


