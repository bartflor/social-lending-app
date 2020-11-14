package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent
import pl.fintech.solidlending.solidlendigplatform.domain.common.TimeService
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.LenderRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRiskService
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class AuctionDomainServiceImplTest extends Specification {
	def auctionRepo = Mock(AuctionRepository)
	def borrowerRepo = Mock(BorrowerRepository)
	def offerRepo = Mock(OfferRepository)
	def lenderRepo = Mock(LenderRepository)
	def loanRiskSvc = Mock(LoanRiskService)
	def timeSvc = Mock(TimeService)

	@Subject
	def domainSvc = new AuctionDomainServiceImpl(auctionRepo, borrowerRepo, offerRepo, lenderRepo, loanRiskSvc, timeSvc)

	def "endAuction should call end() on auction with given id and \
		update repository and\
		return EndAuctionEvent with offers selected"(){
		given:
			def randId = Gen.integer.first()
			def auction = Mock(Auction)
			auctionRepo.findById(randId) >> Optional.of(auction)
			def policy = Mock(OffersSelectionPolicy)
			def event = GroovyMock(EndAuctionEvent)
		when:
			def result = domainSvc.endAuction(randId, policy)
		then:
			1*auction.end(policy) >> event
			1*auctionRepo.updateAuction(randId, auction)
			1*auctionRepo.findAuctionOffers(randId) >> Collections.emptyList()
		and:
			result == event

	}
}
