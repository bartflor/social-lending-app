package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException
import pl.fintech.solidlending.solidlendigplatform.domain.common.TimeService
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.LenderRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRiskService
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.Period

class AuctionDomainServiceImplMockedUniTest extends Specification {

	@Subject
	AuctionDomainService auctionService
	AuctionRepository auctionRepo
	BorrowerRepository borrowerRepo
	LenderRepository lenderRepo
	OfferRepository offerRepo
	LoanRiskService loanRiskSvc
	TimeService timeService


	def setup() {
		auctionRepo = Mock()
		borrowerRepo = Mock()
		lenderRepo = Mock()
		offerRepo = Mock()
		loanRiskSvc = Mock()
		timeService = Mock()
		auctionService = new AuctionDomainServiceImpl(auctionRepo,
				borrowerRepo,
				offerRepo,
				lenderRepo,
				loanRiskSvc,
				timeService)
	}

	def "crateNewAuction method should save new Auction"() {
		given:
			def borrowerName = Gen.string.iterator().next()
			def auctionDuration = Period.ofDays(Gen.integer.iterator().next())
			def amount = Gen.integer.iterator().next()
			def loanDuration = Period.ofDays(Gen.integer.iterator().next())
			def rate = Gen.getDouble().iterator().next()
			def loanStart = Gen.date.first().toInstant()
			def rating = Gen.integer.iterator().next()
			def now = Gen.date.first().toInstant()
			timeService.now() >> now
			Borrower borrower = Mock()
			borrower.getRating() >> new Rating(rating)
			borrowerRepo.findBorrowerByUserName(borrowerName) >> Optional.of(borrower)
			def auction = AuctionDomainFactory.createAuction(borrowerName,
															auctionDuration,
															amount,
															loanDuration,
															rate,
															loanStart,
															rating,
															now)

		when:
			auctionService.createNewAuction(borrowerName,
					auctionDuration,
					amount,
					loanDuration,
					rate,
					loanStart)
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
			def loanStart = Gen.date.first().toInstant()
			borrowerRepo.findBorrowerByUserName(borrowerName) >> Optional.empty()
		when:
			auctionService.createNewAuction(borrowerName,
					auctionDuration,
					amount,
					loanDuration,
					rate,
					loanStart)
		then:
			thrown(AuctionCreationException)

	}

}
