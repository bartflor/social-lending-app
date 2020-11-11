package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent
import spock.genesis.Gen
import spock.lang.Specification

class AuctionTest extends Specification {

	def "AddNewOffer should update status if offers total amount is greater than requested loan amount"() {
		given:
			def randInt = Gen.integer(0, 10000).first()
			def offer1 = AuctionDomainFactory.createOfferWithAmount(randInt, randInt)
			def offer2 = AuctionDomainFactory.createOfferWithAmount(randInt, randInt)
			def auction = AuctionDomainFactory.createAuctionWithAmount(randInt+1)
		when:
			auction.addNewOffer(offer1)
			auction.addNewOffer(offer2)
		then:
			auction.getOffers().size() == 2
		and:
			auction.getStatus() == Auction.AuctionStatus.ACTIVE_COMPLETE
	}

	def "AddNewOffer should not change status if offers total amount is lower than requested loan amount"() {
		given:
			def randInt = Gen.integer(0, 10000).first()
			def offer1 = AuctionDomainFactory.createOfferWithAmount(randInt, randInt)
			def offer2 = AuctionDomainFactory.createOfferWithAmount(randInt, randInt)
			def auction = AuctionDomainFactory.createAuctionWithAmount(3*randInt)
		when:
			auction.addNewOffer(offer1)
			auction.addNewOffer(offer2)
		then:
			auction.getOffers().size() == 2
		and:
			auction.getStatus() == Auction.AuctionStatus.ACTIVE
	}

	def "Auction.end() should change status to ARCHIVED, and \
		return EndAuctionEvent"(){
		given:
			def randInt = Gen.integer.first()
			def auction = AuctionDomainFactory.createActiveCompleteAuction(randInt)
			def policy = Mock(OffersSelectionPolicy)
			def selectedOffers = Set.of(AuctionDomainFactory.createOfferWithAmount(randInt, randInt))
		when:
			def result = auction.end(policy)
		then:
			1* policy.selectOffers(auction.getOffers(), auction.getAuctionLoanParams()) >> selectedOffers
			auction.getStatus() == Auction.AuctionStatus.ARCHIVED
		and:
			result == EndAuctionEvent.builder()
					.BorrowerUserName(auction.getBorrowerUserName())
					.offers(selectedOffers)
					.auctionLoanParams(auction.getAuctionLoanParams())
					.build();
	}

}
