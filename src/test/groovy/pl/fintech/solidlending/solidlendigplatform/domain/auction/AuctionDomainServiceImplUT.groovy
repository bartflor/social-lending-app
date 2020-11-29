package pl.fintech.solidlending.solidlendigplatform.domain.auction


import pl.fintech.solidlending.solidlendigplatform.domain.common.TimeService
import pl.fintech.solidlending.solidlendigplatform.domain.common.UserService
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.EndAuctionEvent
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import spock.genesis.Gen
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Subject

import java.time.Period
import java.time.temporal.ChronoUnit

class AuctionDomainServiceImplUT extends Specification {
	def auctionRepo = Mock(AuctionRepository)
	def userSvc = Mock(UserService)
	def offerRepo = Mock(OfferRepository)
	def timeSvc = Mock(TimeService)

	@Subject
	def auctionService = new AuctionDomainServiceImpl(auctionRepo, offerRepo, timeSvc, userSvc)

	def "endAuction should end auction with given id and return EndAuctionEvent with offers selected"(){
		given: "auction to end in repository"
			def randId = Gen.integer.first()
			def auction = Mock(Auction)
			auctionRepo.findById(randId) >> Optional.of(auction)
			def policy = Mock(OffersSelectionPolicy)
			def event = GroovyMock(EndAuctionEvent)
		when: "call endAuction() with given parameters"
			def result = auctionService.endAuction(randId, policy)
		then: "should select auction with given id from repository, end auction and update repository"
			1*auction.end(policy) >> event
			1*auctionRepo.updateAuction(randId, auction)
			1*auctionRepo.findAuctionOffers(randId) >> Collections.emptyList()
		and: "should return proper event object"
			result == event

	}

	def "crateNewAuction method should save new created Auction"() {
		given: "any parameters"
			def borrowerName = Gen.string.first()
			def auctionDuration = Period.ofDays(Gen.integer.first())
			def amount = Gen.integer.first()
			def loanDuration = Period.ofDays(Gen.integer.first())
			def rate = Gen.getDouble().first()
			def rating = Gen.integer.first()
			def now = Gen.date.first().toInstant()
			timeSvc.now() >> now
			Borrower borrower = AuctionsTestsHelper.createBorrower(borrowerName, rating)
			userSvc.findBorrower(borrowerName) >> borrower
			def auction = AuctionsTestsHelper.createAuction(borrowerName,
					auctionDuration,
					amount,
					loanDuration,
					rate,
					rating,
					now.plus(auctionDuration.getDays(), ChronoUnit.DAYS))

		when: "call createNewAuction with given parameters"
			auctionService.createNewAuction(borrowerName,
					auctionDuration,
					amount,
					loanDuration,
					rate)
		then:"should save proper object to repository"
			1 * auctionRepo.save(_) >> { arguments -> Auction given = arguments.get(0)
				assert given == (auction) }
	}

	def "addOffer should create new offer object from given parameters, add to auction and update repositories"(){
		given:
			def auctionId = Gen.long.first()
			def offerId = Gen.long.first()
			def lenderName = Gen.string(20).first()
			def borrowerName = Gen.string(20).first()
			def amount = Gen.integer(0, Integer.MAX_VALUE).first()
			def rate = Gen.integer(0, 100).first()
			def duration = Period.ofMonths(Gen.integer(0, Integer.MAX_VALUE).first())
			def auction = Mock(Auction)
			auction.getBorrower() >> AuctionsTestsHelper.createBorrower(borrowerName, Gen.integer(0, 5).first() as double)
			auction.getLoanDuration() >> duration
			def expectedOffer = Offer.builder()
					.auctionId(auctionId)
					.lenderName(lenderName)
					.amount(new Money(amount))
					.rate(Rate.fromPercentValue(rate))
					.duration(duration)
					.build()
		when:
			def result = auctionService.addOffer(auctionId, lenderName, amount, rate)
		then:
			1 * userSvc.lenderExists(lenderName) >> true
			1 * auctionRepo.findById(auctionId) >> Optional.of(auction)
			1 * auction.addNewOffer(_) >> { arguments -> Offer given = arguments.get(0)
											assert given == expectedOffer }
			1 * offerRepo.save(expectedOffer) >>  offerId
			1 * auctionRepo.updateAuction(auctionId, auction)
		and:
			result == offerId
	}

}
