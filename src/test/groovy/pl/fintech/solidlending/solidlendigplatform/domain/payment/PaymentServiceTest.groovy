package pl.fintech.solidlending.solidlendigplatform.domain.payment

import pl.fintech.solidlending.solidlendigplatform.domain.common.events.ExternalTransferOrderEvent
import pl.fintech.solidlending.solidlendigplatform.domain.common.UserService
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.payment.exception.TransferFailException
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.CommunicationDataFactory
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class PaymentServiceTest extends Specification {
	def bankClientMock = Mock(BankClient)
	def userSvc = Mock(UserService)

	@Subject
	def paymentService = new PaymentServiceImpl(bankClientMock, userSvc)

	def "execute() should find users, call BankClient with parameters given with TransferOrderEvent \
		 and return transfer reference id."() {
		given:
			def sourceUserAccount = UUID.randomUUID()
			def targetUserAccount = UUID.randomUUID()
			def refId = UUID.randomUUID().toString()
			def amount = new Money(Gen.integer(0, 10000).first() as double)
			def sourceUserName = Gen.string(5,20).first()
			def targetUserName = Gen.string(5,20).first()
			def sourceUser = PaymentDomainFactory.createLender(sourceUserAccount, sourceUserName)
			def targetUser = PaymentDomainFactory.createBorrower(targetUserAccount, targetUserName)
			def event = PaymentDomainFactory
					.createTransferOrderEvent(sourceUserName, targetUserName, amount)
		when:
			def result = paymentService.execute(event)
		then:
			1 * bankClientMock.transfer(sourceUserAccount, targetUserAccount, amount.getValue().doubleValue()) >> refId
			1 * userSvc.findUser(sourceUserName) >> sourceUser
			1 * userSvc.findUser(targetUserName) >> targetUser
		and:
			result == refId
	}


	def "execute() should throw exception, when sourceUser has no platform bank account"() {
		given:
			def amount = new Money(Gen.integer(0, 10000).first() as double)
			def sourceUserName = Gen.string(5,20).first()
			def targetUserName = Gen.string(5,20).first()
			def sourceUser = PaymentDomainFactory.createLender(null, sourceUserName)
			def targetUser = PaymentDomainFactory.createBorrower(UUID.randomUUID(), targetUserName)

		when:
			paymentService.execute(PaymentDomainFactory.createTransferOrderEvent(sourceUserName, targetUserName, amount))
		then:
			1 * userSvc.findUser(sourceUserName) >> sourceUser
			1 * userSvc.findUser(targetUserName) >> targetUser
			0 * bankClientMock.transfer(_, _, amount)
		and:
			def exception = thrown(TransferFailException)
			exception.getMessage() == "User with provided user names: "+sourceUserName+", " +
					""+targetUserName+" has no platform bank account number specified"

	}
	
	def "executeExternal() should order deposit transfer calling BankClient with orderEvent param \
		given ExternalTransferOrderEvent with deposit type  "(){
		given:
			def platformAccount = UUID.randomUUID()
			def privateAccount = UUID.randomUUID()
			def amount = Gen.integer.first()
			def userName = Gen.string(CommunicationDataFactory.jsonAllowedString).first()
			def event = ExternalTransferOrderEvent.builder()
					.amount(new Money(amount))
					.userName(userName)
					.transferType(ExternalTransferOrderEvent.TransferType.DEPOSIT)
					.build()
		when:
			paymentService.executeExternal(event)
		then:
			1*userSvc.findUser(userName) >> PaymentDomainFactory.createUser(platformAccount, privateAccount)
			1*bankClientMock.transfer(privateAccount, platformAccount, amount)
	}

	def "executeExternal() should order withdrawal transfer calling BankClient with orderEvent param \
		given ExternalTransferOrderEvent with withdrawal type  "(){
		given:
			def platformAccount = UUID.randomUUID()
			def privateAccount = UUID.randomUUID()
			def amount = Gen.integer.first()
			def userName = Gen.string(CommunicationDataFactory.jsonAllowedString).first()
			def event = ExternalTransferOrderEvent.builder()
					.amount(new Money(amount))
					.userName(userName)
					.transferType(ExternalTransferOrderEvent.TransferType.WITHDRAWAL)
					.build()
		when:
			paymentService.executeExternal(event)
		then:
			1*userSvc.findUser(userName) >> PaymentDomainFactory.createUser(platformAccount, privateAccount)
			1*bankClientMock.transfer(platformAccount, privateAccount, amount)
	}


}


