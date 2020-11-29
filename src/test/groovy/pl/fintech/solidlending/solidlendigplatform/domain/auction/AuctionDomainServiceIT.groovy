package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException
import pl.fintech.solidlending.solidlendigplatform.domain.common.TimeService
import pl.fintech.solidlending.solidlendigplatform.domain.common.UserService
import pl.fintech.solidlending.solidlendigplatform.domain.common.UserServiceImpl
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.exception.UserNotFoundException
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.*
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory.InMemoryAuctionRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory.InMemoryOfferRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory.InMemoryUserRepo
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant
import java.time.Period

class AuctionDomainServiceIT extends Specification {

	@Subject
	AuctionDomainServiceImpl auctionService

	AuctionRepository auctionRepo
	BorrowerRepository borrowerRepo
	LenderRepository lenderRepo
	UserService userService
	OfferRepository offerRepo
	TimeService timeService
	String lenderName
	String borrowerName
	
	def setup() {
		auctionRepo = new InMemoryAuctionRepo()
		borrowerRepo = new InMemoryUserRepo()
		lenderRepo = borrowerRepo
		userService = new UserServiceImpl(lenderRepo, borrowerRepo)
		offerRepo = new InMemoryOfferRepo()
		timeService = Mock(TimeService)
		lenderName = Gen.string(20).first()
		borrowerName = Gen.string(20).first()
		auctionService = new AuctionDomainServiceImpl(auctionRepo,
				offerRepo,
				timeService,
				userService)
		borrowerRepo.save(Borrower.builder()
				.userDetails(UserDetails.builder()
						.userName(borrowerName)
						.privateAccountNumber(UUID.randomUUID())
						.platformAccountNumber(UUID.randomUUID()).build())
				.rating(new Rating())
				.build())
		lenderRepo.save(Lender.builder()
				.userDetails(UserDetails.builder()
						.userName(lenderName)
						.platformAccountNumber(UUID.randomUUID()).build())
				.build())
		timeService.now() >> Instant.now()
	}

	def "createNewAuction should save new Auction to repository and return new id"() {
		when:
			def loanDuration = Period.ofYears(2)
			def auctionDuration	= Period.of(0, 1, 15)
			def resultId = auctionService.createNewAuction(borrowerName,
					auctionDuration,
					1200.0, loanDuration,
					15.4)
		then:
			auctionRepo.findAll().size() == 1
		and:
			def resultAuction = auctionRepo.findById(resultId).get()
			resultAuction.getBorrower().getUserDetails().getUserName() == borrowerName
			resultAuction.getAuctionDuration() == auctionDuration
			resultAuction.getStatus() == Auction.AuctionStatus.ACTIVE
			resultAuction.getOffers().isEmpty()

	}

	def "addOffer should save new Offer and link it to proper Auction"() {
		given:
			def auction = AuctionsTestsHelper.createAuction()
			def auctionId = auctionRepo.save(auction)
			def offer = AuctionsTestsHelper.createOfferWithLenderNameAuctionId(lenderName, auctionId)
		when:
			auctionService.addOffer(auctionId,
					offer.getLenderName(),
					offer.getAmount().getValue().doubleValue(),
					offer.getRate().getPercentValue())
		then:
			auction.getOffers().size() == 1
			def addedOffer = auction.getOffers().find()
		and:
			addedOffer.getLenderName() == lenderName
			addedOffer.getAuctionId() == auctionId
			addedOffer.getDuration() == auction.getLoanDuration()
			addedOffer.getRate() == offer.getRate()
			addedOffer.getAmount() == offer.getAmount()
	}

	def "addOffer should throw exception if given lender name not found"() {
		given:
			def auction = AuctionsTestsHelper.createAuction()
			def auctionId = auctionRepo.save(auction)
			def offer = AuctionsTestsHelper.createOfferWithLenderNameAuctionId("non_existing_lender_name", auctionId)
		when:
			auctionService.addOffer(auctionId,
					offer.getLenderName(),
					offer.getAmount().getValue().doubleValue(),
					offer.getRate().getPercentValue())
		then:
			thrown(UserNotFoundException)
	}

	def "crateNewAuction method should throw exception when Borrower with given username not exists."() {
		when:
			auctionService.createNewAuction("non_existing_borrower_name",
					Period.of(0, 1, 15),
					1200.0, Period.ofYears(2),
					15.4)
		then:
			thrown(UserNotFoundException)

	}

	def "getPlatformAuction should return all Auction from repository"(){
		given:
			def auction1 = AuctionsTestsHelper.createAuction(borrowerName)
			auctionRepo.save(auction1)
			def auction2 = AuctionsTestsHelper.createAuction("different_borrower_name")
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
			def auction1 = AuctionsTestsHelper.createAuction(borrowerName)
			auctionRepo.save(auction1)
			def auction2 = AuctionsTestsHelper.createAuction("different_borrower_name")
			auctionRepo.save(auction2)
		when:
			def resultList = auctionService.getUserAuctions(borrowerName)
		then:
			resultList.size() == 1
		and:
			resultList.contains(auction1)
	}

	def "getLendersOffers should return all Offers with given LenderName"(){
		given:
			def offer1 = AuctionsTestsHelper.createOfferWithLenderNameAuctionId(lenderName, 1)
			offerRepo.save(offer1)
			def offer2= AuctionsTestsHelper.createOfferWithLenderNameAuctionId("different_lender_name", 1)
			offerRepo.save(offer2)
		when:
			def result = auctionService.getLenderOffers(lenderName)
		then:
			result.size() == 1
		and:
			result.contains(offer1)
	}


}
