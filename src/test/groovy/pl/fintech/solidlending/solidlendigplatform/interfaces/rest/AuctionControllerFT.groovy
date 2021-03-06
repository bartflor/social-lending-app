package pl.fintech.solidlending.solidlendigplatform.interfaces.rest


import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Auction
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionsTestsHelper
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRepository
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.MockPaymentService
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.PostgresContainerTestSpecification
import spock.genesis.Gen

import static org.hamcrest.Matchers.*

import java.time.Period
@Import([MockPaymentService])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuctionControllerFT extends PostgresContainerTestSpecification {

	@LocalServerPort
	int serverPort
	@Autowired
	AuctionRepository auctionRepository
	@Autowired
	LoanRepository loanRepository
	@Autowired
	BorrowerRepository borrowerRepository

	RequestSpecification restClient

	def setup() {
		restClient = RestAssured.given()
				.port(serverPort)
				.accept(MediaType.APPLICATION_JSON.toString())
				.contentType(MediaType.APPLICATION_JSON.toString())
				.log().all()
	}

	def "GET /api/auctions/{auctionId}/create-loan should create new loan in repository"() {
		given:
			def borrowerName = Gen.string(20).first()
			def amount = Gen.integer(0, Integer.MAX_VALUE).first()
			def auction = AuctionsTestsHelper.createAuctionWithUserNameAmount(borrowerName, amount)
			def id = auctionRepository.save(auction)
			auction.addNewOffer(AuctionsTestsHelper.createOfferWithAmount(amount,id))
			auctionRepository.updateAuction(id, auction)
			borrowerRepository.save(AuctionsTestsHelper.createBorrower(borrowerName, Gen.integer(0, 5).first() as double))
		when:
			def response = restClient.get("/api/auctions/" + id + "/create-loan")
		then:
			response.statusCode() == 201
		and:
			loanRepository.findAll().size() == 1
	}
	def "GET /api/auction/{auctionId}/create-loan should response with loan created from auction with {auctionId}, \
		save new loan with status UNCONFIRMED and set ended auction status to ARCHIVED"(){
		given:
			def borrower = Gen.string(20).first()
			def loanDuration = Period.ofMonths(Gen.integer(1, 10).first())
			def amount = Gen.integer(0, 10000).first()
			def rate = Gen.integer(0,100).first()
			def rating = Gen.integer(0, 5).first()
			def auction = AuctionsTestsHelper.createAuction(borrower,
					Period.ofDays(Gen.integer.first()), amount, loanDuration,rate, rating)
			def id = auctionRepository.save(auction)
			auction.addNewOffer(AuctionsTestsHelper.createOfferWithAmount(amount, id))
			auctionRepository.updateAuction(id, auction)
			borrowerRepository.save(AuctionsTestsHelper.createBorrower(borrower, rating))
		when:
			def response = restClient.get("/api/auctions/"+id+"/create-loan")
		then:
			response.statusCode() == 201
			auctionRepository.findById(id).get().getStatus() == Auction.AuctionStatus.ARCHIVED
			loanRepository.findAllByUsername(borrower).size() == 1
			def createdLoan = loanRepository.findAllByUsername(borrower).first()
		and:
			response.then()
					.body("id", equalTo((int)createdLoan.getId()))
					.body("borrowerUserName", equalTo(createdLoan.getBorrowerUserName()))
					.body("amount", equalTo(createdLoan.getAmount().getValue().floatValue()))
					.body("repayment", equalTo(createdLoan.getRepayment().getValue().floatValue()))
					.body("rate", equalTo(createdLoan.getAverageRate().getPercentValue().floatValue()))
					.body("duration", equalTo((int)createdLoan.getDuration().toTotalMonths()))
			createdLoan.getStatus() == Loan.LoanStatus.UNCONFIRMED
			auctionRepository.findById(id).get().getStatus() == Auction.AuctionStatus.ARCHIVED

	}


}
