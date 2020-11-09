package pl.fintech.solidlending.solidlendigplatform.domain.auction

import org.springframework.context.annotation.Import
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanDomainFactory
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class AuctionApplicationServiceImplTest extends Specification {

	def domainService = Mock(AuctionDomainService)
	def loanService = Mock(LoanApplicationService)

	@Subject
	def auctionApplicationService = new AuctionApplicationServiceImpl(domainService, loanService);

	def "createLoanFromEndingAuction should invoke domain services with proper args"(){
		given:
			def randId = Gen.integer.first()
			def policy = new BestOfferRatePolicy()
			def auction = AuctionDomainFactory.createAuction()
		when:
			auctionApplicationService.createLoanFromEndingAuction(randId, policy)
		then:
			1*domainService.endAuction(randId, policy) >> auction
			1*loanService.createLoan(auction)
	}
}
