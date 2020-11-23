package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException
import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent
import pl.fintech.solidlending.solidlendigplatform.domain.common.TimeService
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.LenderRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

import java.time.Period
import java.time.temporal.ChronoUnit

class AuctionDomainServiceImplTest extends Specification {
	def auctionRepo = Mock(AuctionRepository)
	def borrowerRepo = Mock(BorrowerRepository)
	def offerRepo = Mock(OfferRepository)
	def lenderRepo = Mock(LenderRepository)
	def loanRiskSvc = Mock(LoanRiskService)
	def timeSvc = Mock(TimeService)

	@Subject
	def auctionService = new AuctionDomainServiceImpl(auctionRepo, borrowerRepo, offerRepo, lenderRepo, loanRiskSvc, timeSvc)

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
			def result = auctionService.endAuction(randId, policy)
		then:
			1*auction.end(policy) >> event
			1*auctionRepo.updateAuction(randId, auction)
			1*auctionRepo.findAuctionOffers(randId) >> Collections.emptyList()
		and:
			result == event

	}

	def "crateNewAuction method should save new Auction"() {
		given:
			def borrowerName = Gen.string.iterator().next()
			def auctionDuration = Period.ofDays(Gen.integer.iterator().next())
			def amount = Gen.integer.iterator().next()
			def loanDuration = Period.ofDays(Gen.integer.iterator().next())
			def rate = Gen.getDouble().iterator().next()
			def rating = Gen.integer.iterator().next()
			def now = Gen.date.first().toInstant()
			timeSvc.now() >> now
			Borrower borrower = Mock()
			borrower.getRating() >> new Rating(rating)
			borrower.hasLinkedBankAccount() >> true
			borrowerRepo.findBorrowerByUserName(borrowerName) >> Optional.of(borrower)
			def auction = AuctionDomainFactory.createAuction(borrowerName,
					auctionDuration,
					amount,
					loanDuration,
					rate,
					rating,
					now.plus(auctionDuration.getDays(), ChronoUnit.DAYS))

		when:
			auctionService.createNewAuction(borrowerName,
					auctionDuration,
					amount,
					loanDuration,
					rate)
		then:
			1 * auctionRepo.save({ argument -> argument == auction })
	}

	def "crateNewAuction method should throw exception when Borrower with username not exists."() {
		given:
			def borrowerName = Gen.string.iterator().next()
			def auctionDuration = Period.ofDays(Gen.integer.iterator().next())
			def amount = Gen.integer.iterator().next()
			def loanDuration = Period.ofDays(Gen.integer.iterator().next())
			def rate = Gen.getDouble().iterator().next()
			borrowerRepo.findBorrowerByUserName(borrowerName) >> Optional.empty()
		when:
			auctionService.createNewAuction(borrowerName,
					auctionDuration,
					amount,
					loanDuration,
					rate)
		then:
			thrown(AuctionCreationException)

	}

	def "addOffer should create new offer object from given parameters, add to auction and update repositories"(){
		given:
			def auctionId = Gen.long.first()
			def offerId = Gen.long.first()
			def lenderName = Gen.string(20).first()
			def borrowerName = Gen.string(20).first()
			def amount = Gen.integer(0, Integer.MAX_VALUE).first()
			def rate = Gen.integer(0, 100).first()
			def risk = new Risk(Gen.integer(0, Integer.MAX_VALUE).first())
			def duration = Period.ofMonths(Gen.integer(0, Integer.MAX_VALUE).first())
			def auction = Mock(Auction)
			def loanParams = Mock(AuctionLoanParams)
			loanParams.getLoanRisk() >> risk
			loanParams.getLoanDuration() >> duration
			auction.getBorrowerUserName() >> borrowerName
			auction.getAuctionLoanParams() >> loanParams
			auction.getAuctionLoanParams() >> duration
			def expectedOffer = Offer.builder()
					.auctionId(auctionId)
					.lenderName(lenderName)
					.borrowerName(borrowerName)
					.amount(new Money(amount))
					.risk(risk)
					.rate(Rate.fromPercentValue(rate))
					.duration(duration)
					.build();
		when:
			def result = auctionService.addOffer(auctionId, lenderName, amount, rate)
		then:
			1 * lenderRepo.lenderExist(lenderName) >> true
			1 * auctionRepo.findById(auctionId) >> Optional.of(auction)
			1 * auction.addNewOffer(_) >> { arguments -> Offer given = arguments.get(0)
											assert given == (expectedOffer) }
			1 * offerRepo.save(expectedOffer) >>  offerId
			1 * auctionRepo.updateAuction(auctionId, auction)
		and:
			result == offerId
	}
}
