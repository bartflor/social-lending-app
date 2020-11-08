package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.*
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRiskService
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction.InMemoryAuctionRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction.InMemoryOfferRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user.InMemoryUserRepo
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.Period

class AuctionDomainServiceImplUnitTest extends Specification {

	@Subject
	AuctionDomainServiceImpl auctionService

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
		auctionService = new AuctionDomainServiceImpl(auctionRepo,
				borrowerRepo,
				offerRepo,
				lenderRepo,
				loanRiskSvc)
		borrowerRepo.save(Borrower.builder()
				.userDetails(new UserDetails("borrower_name", "borrower@mail", UUID.randomUUID().toString()))
				.rating(new Rating(3))
				.balance(new Money(BigDecimal.ZERO))
				.build())
		lenderRepo.save(Lender.builder()
				.balance(new Money(10))
				.userDetails(new UserDetails("lender_name", "lender@mail", UUID.randomUUID().toString()))
				.build())
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
			def auction = AuctionDomainFactory.createAuction()
			def auctionId = auctionRepo.save(auction)
			def offer = AuctionDomainFactory.createOffer("lender_name", auctionId)
		when:
			auctionService.addOffer(offer)
		then:
			auction.getOffers().size() == 1
		and:
			auction.getOffers().find() == offer
	}

	def "addOffer should throw exception if given lender name not found"() {
		given:
			def auction = AuctionDomainFactory.createAuction()
			def auctionId = auctionRepo.save(auction)
			def offer = AuctionDomainFactory.createOffer("non_existing_lender_name", auctionId)
		when:
			auctionService.addOffer(offer)
		then:
			thrown(UserNotFoundException)
	}

	def "crateNewAuction method should throw exception when Borrower with username not exists."() {
		when:
			auctionService.createNewAuction("non_existing_borrower_name",
					Period.of(0, 1, 15),
					1200.0, Period.ofYears(2),
					15.4, LocalDate.now())
		then:
			thrown(AuctionCreationException)

	}

	def "getPlatformAuction should return all Auction from repository"(){
		given:
			def auction1 = AuctionDomainFactory.createAuction("borrower_name")
			auctionRepo.save(auction1)
			def auction2 = AuctionDomainFactory.createAuction("different_borrower_name")
			auctionRepo.save(auction2)
		when:
			def resultList = auctionService.getPlatformAuctions()
		then:
			resultList.size() == 2
		and:
			resultList.contains(auction1)
			resultList.contains(auction2)
	}

	def "getUserAuction should return all Auction with given userName from repository"(){
		given:
			def auction1 = AuctionDomainFactory.createAuction("borrower_name")
			auctionRepo.save(auction1)
			def auction2 = AuctionDomainFactory.createAuction("different_borrower_name")
			auctionRepo.save(auction2)
		when:
			def resultList = auctionService.getUserAuctions("borrower_name")
		then:
			resultList.size() == 1
		and:
			resultList.contains(auction1)
	}

	def "getLendersOffers should return all Offers with given LenderName"(){
		given:
			def offer1 = AuctionDomainFactory.createOffer("lender_name", 1)
			offerRepo.save(offer1)
			def offer2= AuctionDomainFactory.createOffer("different_lender_name", 1)
			offerRepo.save(offer2)
		when:
			def result = auctionService.getLenderOffers("lender_name")
		then:
			result.size() == 1
		and:
			result.contains(offer1)
	}
}
