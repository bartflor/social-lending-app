package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent
import spock.genesis.Gen
import spock.lang.Specification

class AuctionUT extends Specification {

	def "AddNewOffer should update status if offers total amount is greater than requested loan amount"() {
		given:
			def randInt = Gen.integer(0, 10000).first()
			def offer1 = AuctionsTestsHelper.createOfferWithAmount(randInt, Gen.getLong().first())
			def offer2 = AuctionsTestsHelper.createOfferWithAmount(randInt, Gen.getLong().first())
			def auction = AuctionsTestsHelper.createAuctionWithAmount(randInt+1)
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
			def offer1 = AuctionsTestsHelper.createOfferWithAmount(randInt, Gen.getLong().first())
			def offer2 = AuctionsTestsHelper.createOfferWithAmount(randInt, Gen.getLong().first())
			def auction = AuctionsTestsHelper.createAuctionWithAmount(3*randInt)
		when:
			auction.addNewOffer(offer1)
			auction.addNewOffer(offer2)
		then:
			auction.getOffers().size() == 2
		and:
			auction.getStatus() == Auction.AuctionStatus.ACTIVE
	}

	def "AddNewOffer should update status if offers total amount is equal requested loan amount"() {
		given:
			def randInt1 = Gen.integer(0, 10000).first()
			def randInt2 = Gen.integer(0, 10000).first()
			def offer1 = AuctionsTestsHelper.createOfferWithAmount(randInt1, Gen.getLong().first())
			def offer2 = AuctionsTestsHelper.createOfferWithAmount(randInt2, Gen.getLong().first())
			def auction = AuctionsTestsHelper.createAuctionWithAmount(randInt1+randInt2)
		when:
			auction.addNewOffer(offer1)
			auction.addNewOffer(offer2)
		then:
			auction.getOffers().size() == 2
		and:
			auction.getStatus() == Auction.AuctionStatus.ACTIVE_COMPLETE
	}

	def "Auction.end() should change status to ARCHIVED, and \
		return EndAuctionEvent"(){
		given:
			def randInt = Gen.integer.first()
			def auction = AuctionsTestsHelper.createActiveCompleteAuction(randInt)
			def policy = Mock(OffersSelectionPolicy)
			def selectedOffers = Set.of(AuctionsTestsHelper.createOfferWithAmount(randInt, randInt))
		when:
			def result = auction.end(policy)
		then:
			1* policy.selectOffers(auction.getOffers(), auction.getAuctionLoanParams()) >> selectedOffers
			auction.getStatus() == Auction.AuctionStatus.ARCHIVED
		and:
			result == EndAuctionEvent.builder()
					.BorrowerUserName(auction.getBorrower().getUserDetails().getUserName())
					.offers(selectedOffers)
					.auctionLoanParams(auction.getAuctionLoanParams())
					.build()
	}

}
