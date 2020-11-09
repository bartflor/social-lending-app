package pl.fintech.solidlending.solidlendigplatform.domain.auction

import spock.genesis.Gen
import spock.lang.Specification

class AuctionTest extends Specification {

	def "AddNewOffer should update status if offers total amount is greater than requested loan amount"() {
		given:
			def randInt = Gen.integer(0, 10000).first()
			def offer1 = AuctionDomainFactory.createOfferWithAmount(randInt)
			def offer2 = AuctionDomainFactory.createOfferWithAmount(randInt)
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
			def offer1 = AuctionDomainFactory.createOfferWithAmount(randInt)
			def offer2 = AuctionDomainFactory.createOfferWithAmount(randInt)
			def auction = AuctionDomainFactory.createAuctionWithAmount(3*randInt)
		when:
			auction.addNewOffer(offer1)
			auction.addNewOffer(offer2)
		then:
			auction.getOffers().size() == 2
		and:
			auction.getStatus() == Auction.AuctionStatus.ACTIVE
	}
}
