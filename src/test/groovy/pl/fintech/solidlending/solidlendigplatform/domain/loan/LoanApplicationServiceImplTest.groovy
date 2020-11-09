package pl.fintech.solidlending.solidlendigplatform.domain.loan

import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionDomainFactory
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class LoanApplicationServiceImplTest extends Specification {

	def loanDomainSvcMock = Mock(LoanDomainService)
	@Subject
	def loanAppSvc = new LoanApplicationServiceImpl(loanDomainSvcMock)

	def "createLoan should crate new loan from given auction"(){
		given:
			def auction = AuctionDomainFactory.createAuction()
			def loanParams = LoanParams.builder()
					.borrowerUserName(auction.getBorrowerUserName())
					.investments(Collections.emptySet())
					.loanAmount(auction.getAuctionLoanParams().getLoanAmount())
					.loanDuration(auction.getAuctionLoanParams().getLoanDuration())
					.loanStartDate(auction.getAuctionLoanParams().getLoanStartDate())
					.build()
		when:
			loanAppSvc.createLoan(auction)
		then:
			1*loanDomainSvcMock.createLoan(loanParams)
	}

	def "activateLoan should call domainSvc method with given id"(){
		given:
			def randId = Gen.integer.first()
		when:
			loanAppSvc.activateLoan(randId)
		then:
			1*loanDomainSvcMock.activateLoan(randId)
	}
}
