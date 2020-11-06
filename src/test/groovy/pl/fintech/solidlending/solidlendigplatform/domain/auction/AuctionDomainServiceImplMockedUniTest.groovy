package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException
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

	def borrowerName = Gen.string.iterator().next()
	def auctionDuration = Period.ofDays(Gen.integer.iterator().next())
	def amount = Gen.integer.iterator().next()
	def loanDuration = Period.ofDays(Gen.integer.iterator().next())
	def rate = Gen.getDouble().iterator().next()
	def loanStart = LocalDate.ofYearDay(Gen.integer(1900..2020).iterator().next(), Gen.integer(1..365).iterator().next())
	def rating = Gen.integer.iterator().next()

	def setup() {
		auctionRepo = Mock()
		borrowerRepo = Mock()
		lenderRepo = Mock()
		offerRepo = Mock()
		loanRiskSvc = Mock()
		auctionService = new AuctionDomainServiceImpl(auctionRepo,
				borrowerRepo,
				offerRepo,
				lenderRepo,
				loanRiskSvc)
	}

	def "crateNewAuction method should save new Auction"() {
		given:
			Borrower borrower = Mock()
			borrower.getRating() >> new Rating(rating)
			borrowerRepo.findBorrowerByUserName(borrowerName) >> Optional.of(borrower)
			loanRiskSvc.estimateLoanRisk(_) >> null
			def auction = AuctionDomainFactory.createAuction(borrowerName,
															auctionDuration,
															amount,
															loanDuration,
															rate,
															loanStart,
															rating)

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
