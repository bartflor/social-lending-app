package pl.fintech.solidlending.solidlendigplatform.domain.auction

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

	@Subject
	def domainSvc = new AuctionDomainServiceImpl(auctionRepo, borrowerRepo, offerRepo, lenderRepo, loanRiskSvc)

	def "endAuction should archive auction having ACTIVE_COMPLETE status with given id and \
		return auction with offers selected"(){
		given:
			def randId = Gen.integer.first()
			def auction = AuctionDomainFactory.createAuction(randId)
			auction.updateStatus(Auction.AuctionStatus.ACTIVE_COMPLETE)
			auctionRepo.findById(randId) >> Optional.of(auction)
			def policy = Mock(OffersSelectionPolicy)
		when:
			def result = domainSvc.endAuction(randId, policy)
		then:
			1*auctionRepo.archive(randId)
		and:
			1*policy.selectOffers(auction.getOffers(), auction.getAuctionLoanParams()) >> Collections.emptySet()
			result == auction

	}
}
