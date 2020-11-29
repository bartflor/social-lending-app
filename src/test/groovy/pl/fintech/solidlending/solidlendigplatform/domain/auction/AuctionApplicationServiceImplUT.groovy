package pl.fintech.solidlending.solidlendigplatform.domain.auction


import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class AuctionApplicationServiceImplUT extends Specification {

	def domainService = Mock(AuctionDomainService)
	def loanService = Mock(LoanApplicationService)
	def policyMock = Mock(OffersSelectionPolicy)

	@Subject
	def auctionApplicationService = new AuctionApplicationServiceImpl(domainService, loanService);

	def "createLoanFromEndingAuction should invoke domain services with proper args"(){
		given:
			def randId = Gen.integer.first()
			def event = AuctionsTestsHelper.createEndAuctionEvent()
		when:
			auctionApplicationService.createLoanFromEndingAuction(randId, policyMock)
		then:
			1*domainService.endAuction(randId, policyMock) >> event
			1*loanService.createLoan(event)
	}
}
