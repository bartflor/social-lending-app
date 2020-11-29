package pl.fintech.solidlending.solidlendigplatform.domain.common

import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Lender
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.LenderRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.payment.PaymentDomainFactory
import spock.genesis.Gen
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Subject

class UserServiceTest extends Specification {

	def lenderRepoMock = Mock(LenderRepository)
	def borrowerRepoMock = Mock(BorrowerRepository)
	@Subject
	def userSvc = new UserServiceImpl(lenderRepoMock, borrowerRepoMock)

	def "findUser should return existing borrower from repository, given borrower name"(){
		given:
			def borrowerName = Gen.string(5,20).first()
			def borrower = PaymentDomainFactory.createBorrower(UUID.randomUUID(), borrowerName)
		when:
			def result = userSvc.findUser(borrowerName)
		then:
			1 * borrowerRepoMock.findBorrowerByUserName(borrowerName) >> Optional.of(borrower)
			1 * lenderRepoMock.findLenderByUserName(borrowerName) >> Optional.empty()
		and:
			result == borrower
	}

	def "findUser should return existing lender from repository, given lender name"(){
		given:
			def lenderName = Gen.string(5,20).first()
			def lender = PaymentDomainFactory.createLender(UUID.randomUUID(), lenderName)
		when:
			def result = userSvc.findUser(lenderName)
		then:
			1 * borrowerRepoMock.findBorrowerByUserName(lenderName) >> Optional.empty()
			1 * lenderRepoMock.findLenderByUserName(lenderName) >> Optional.of(lender)
		and:
			result == lender
	}


	def "findUser() should throw exception, user with given user name not exists"() {
		given:
			def userName = Gen.string(5,20).first()
		when:
			userSvc.findUser(userName)
		then:
			1 * lenderRepoMock.findLenderByUserName(userName) >> Optional.empty()
			1 * borrowerRepoMock.findBorrowerByUserName(userName) >> Optional.empty()
		and:
			def exception = thrown(UserNotFoundException)
			exception.getMessage() == "User with username:"+userName+" not found."
	}
	def "partial update should call repository with proper parameters"(){
		given:
			def lenderName = Gen.string(5,20).first()
			def lender = PaymentDomainFactory.createLender(UUID.randomUUID(), lenderName)
			Map<String, String> details = Map.of(Gen.string(10).first(), Gen.string(10).first())
			lenderRepoMock.findLenderByUserName(lenderName) >> Optional.of(lender)
			borrowerRepoMock.findBorrowerByUserName(lenderName) >> Optional.empty()
		when:
			userSvc.partialUpdateUserDetails(lenderName, details)
		then:
			lenderRepoMock.updateLenderDetails(_) >>
					{args -> Lender argLender = args.get(0)
					Map argDetails = args.get(1)
					argLender == lender
					argDetails == details}

	}

}
