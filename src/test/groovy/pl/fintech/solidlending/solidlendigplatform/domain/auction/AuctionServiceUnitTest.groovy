package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.auction.exception.AuctionCreationException
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRiskService
import pl.fintech.solidlending.solidlendigplatform.domain.user.Borrower
import pl.fintech.solidlending.solidlendigplatform.domain.user.BorrowerRepository
import pl.fintech.solidlending.solidlendigplatform.domain.user.LenderRepository
import spock.lang.Subject

import java.time.LocalDate
import java.time.Period

class AuctionServiceUnitTest extends spock.lang.Specification {

	AuctionRepository auctionRepo = Mock()
	BorrowerRepository borrowerRepo = Mock()
	LenderRepository lenderRepo = Mock()
	OfferRepository offerRepo = Mock()
	LoanRiskService loanRiskSvc = Mock()

	@Subject
	AuctionService auctionService = new AuctionService(auctionRepo,
			borrowerRepo,
			offerRepo,
			lenderRepo,
			loanRiskSvc)


	def "crateNewAuction method should save new Auction"() {
		given:


			Borrower borrower = Mock()
			borrower.getRating() >> new Rating(12)
			borrowerRepo.findBorrowerByUserName("borrower_name") >> Optional.of(borrower)
			loanRiskSvc.estimateLoanRisk(_) >> new Risk(10)
		when:
			auctionService.createNewAuction("borrower_name",
					Period.of(0, 1, 15),
					1200.0, Period.ofYears(2),
					15.4, LocalDate.now())
		then:
			1 * auctionRepo.save(_)
	}

	def "crateNewAuction method should throw exception when Borrower with username not exists."() {
		given:
			borrowerRepo.findBorrowerByUserName("borrower_name") >> Optional.empty()
		when:
			auctionService.createNewAuction("borrower_name",
					Period.of(0, 1, 15),
					1200.0, Period.ofYears(2),
					15.4, LocalDate.now())
		then:
			thrown(AuctionCreationException)

	}
}
