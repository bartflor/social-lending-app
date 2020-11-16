package pl.fintech.solidlending.solidlendigplatform.domain.loan

import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionDomainFactory
import pl.fintech.solidlending.solidlendigplatform.domain.common.TimeService
import pl.fintech.solidlending.solidlendigplatform.domain.payment.TransferService
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class LoanApplicationServiceImplTest extends Specification {

	def loanDomainSvcMock = Mock(LoanDomainService)
	def transferSvcMock = Mock(TransferService)
	def timeSvcMock = Mock(TimeService)
	@Subject
	def loanAppSvc = new LoanApplicationServiceImpl(loanDomainSvcMock, transferSvcMock, timeSvcMock)

	def "createLoan should crate new loan from given endAuctionEvent"(){
		given:
			def event = AuctionDomainFactory.createEndAuctionEvent()
			def loanParams = NewLoanParams.builder()
					.borrowerUserName(event.getBorrowerUserName())
					.investmentsParams(Collections.emptyList())
					.loanAmount(event.getAuctionLoanParams().getLoanAmount())
					.loanDuration(event.getAuctionLoanParams().getLoanDuration())
					.build()
		when:
			loanAppSvc.createLoan(event)
		then:
			1*loanDomainSvcMock.createLoan(loanParams)
	}

	def "activateLoan should call domainSvc method with given id and call \
		execute on transferService with proper transferOrderEventsList"(){
		given:
			def randId = Gen.integer.first()
			def loan = LoanDomainFactory.crateLoan(randId)
		when:
			loanAppSvc.activateLoan(randId)
		then:
			1*loanDomainSvcMock.findLoanById(randId) >> loan
			1*loanDomainSvcMock.activateLoan(randId)
	}
}
