package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRiskService
import pl.fintech.solidlending.solidlendigplatform.domain.user.Borrower
import pl.fintech.solidlending.solidlendigplatform.domain.user.BorrowerRepository
import pl.fintech.solidlending.solidlendigplatform.domain.user.LenderRepository
import pl.fintech.solidlending.solidlendigplatform.domain.user.UserDetails
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction.InMemoryAuctionRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction.InMemoryOfferRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user.InMemoryUserRepo
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.Period

class AuctionServiceIntegrationTest extends Specification {
	@Subject
	AuctionService auctionService

	AuctionRepository auctionRepo
	BorrowerRepository borrowerRepo
	LenderRepository lenderRepo
	OfferRepository offerRepo
	LoanRiskService loanRiskSvc

	def setup() {
		auctionRepo = new InMemoryAuctionRepo()
		borrowerRepo = new InMemoryUserRepo()
		lenderRepo = borrowerRepo
		offerRepo = new InMemoryOfferRepo()
		loanRiskSvc = new LoanRiskService()
		auctionService = new AuctionService(auctionRepo,
				borrowerRepo,
				offerRepo,
				lenderRepo,
				loanRiskSvc)
		borrowerRepo.save(Borrower.builder()
				.userDetails(new UserDetails("borrower_name", "borrower@mail", UUID.randomUUID().toString()))
				.rating(new Rating(3))
				.balance(new Money(BigDecimal.ZERO))
				.build());
	}

	def "createNewAuction should save new Auction to repository and return new id"() {
		when:
			def resultId = auctionService.createNewAuction("borrower_name",
					Period.of(0, 1, 15),
					1200.0, Period.ofYears(2),
					15.4, LocalDate.now())
		then:
			auctionRepo.findAll().size() == 1
		and:
			def resultAuction = auctionRepo.findById(resultId).get()
			resultAuction.getBorrowerUserName() == "borrower_name"
			resultAuction.getAuctionDuration() == Period.of(0, 1, 15)
			resultAuction.getStartDate() == LocalDate.now()
			resultAuction.getStatus() == Auction.AuctionStatus.ACTIVE
			resultAuction.getOffers().isEmpty()

	}

	def "addOffer should save new Offer and link to proper Auction"() {
		given:
			def auction = Auction.builder()
					.auctionDuration(Period.ofDays(7))
					.borrowerRating(new Rating(1))
					.borrowerUserName("borrower_name")
					.loanParams(LoanParams.builder()
							.loanAmount(new Money(100))
							.loanStartDate(LocalDate.now().plus(10))
							.build())
					.startDate(LocalDate.now())
					.status(Auction.AuctionStatus.ACTIVE)
					.build()
			def auctionId = auctionRepo.save(auction)
			def offer = Offer.builder()
					.auctionId(auctionId)
					.lenderName("testLender")
					.amount(new Money(20))
					.rate(new Rate(2))
					.duration(Period.of(1, 0, 0))
					.build()
		when:
			auctionService.addOffer(offer)
		then:
			auction.getOffers().size() == 1
		and:
			auction.getOffers().find() == offer

	}
}
