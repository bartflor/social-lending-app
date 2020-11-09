package pl.fintech.solidlending.solidlendigplatform.domain.loan

import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionDomainFactory
import spock.lang.Specification
import spock.lang.Subject

class LoanApplicationServiceImplTest extends Specification {

	def loanDomainSvc = Mock(LoanDomainService)
	@Subject
	def loanAppSvc = new LoanApplicationServiceImpl(loanDomainSvc)

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
			1*loanDomainSvc.createLoan(loanParams)
	}
}
