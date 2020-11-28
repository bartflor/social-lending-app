package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating

import spock.genesis.Gen

import java.time.Instant
import java.time.Period

class AuctionsTestsHelper {
	static Auction createAuction(String borrowerName,
	                             Period auctionDuration,
	                             int amount,
	                             Period loanDuration,
	                             double rate,
	                             int rating) {
		def auctionId = Gen.long.first()
		Auction.builder()
				.id(auctionId)
				.borrower(createBorrower(borrowerName, rating))
				.endDate(Instant.now())
				.auctionDuration(auctionDuration)
				.auctionLoanParams(AuctionLoanParams.builder()
						.loanAmount(new Money(amount))
						.loanDuration(loanDuration)
						.loanRate(Rate.fromPercentValue(rate))
						.build())
				.status(Auction.AuctionStatus.ACTIVE)
				.build()
	}

	static Auction createAuction(long id) {
		Auction.builder()
				.id(id)
				.borrower(createBorrower(Gen.string(20).first(), Gen.integer(1,5).first() as double))
				.endDate(Gen.date.first().toInstant())
				.auctionDuration(Period.ofDays(Gen.integer(1..365).first()))
				.auctionLoanParams(AuctionLoanParams.builder()
						.loanAmount(new Money(Gen.double.first()))
						.loanDuration(Period.ofMonths(Gen.integer(1..36).first()))
						.loanRate(Rate.fromPercentValue(Gen.integer(0..100).first()))
						.build())
				.offers(Collections.emptySet())
				.status(Auction.AuctionStatus.ACTIVE)
				.build()
	}
	
	static Auction createAuction(String... borrowerName) {
		def name = borrowerName.length==0? Gen.string(20).first() : borrowerName[0]
		createAuction(name,
				Period.ofDays(Gen.integer.first()),
				Gen.integer.first(),
				Period.ofDays(Gen.integer.first()),
				Gen.getDouble().first(),
				Gen.integer.first())
	}

	static Auction createActiveCompleteAuction(long id) {
		def amount = Gen.integer.first();
		Auction.builder()
				.id(id)
				.borrower(createBorrower(Gen.string(20).first(), Gen.integer(1,5).first() as double))
				.endDate(Gen.date.first().toInstant())
				.auctionDuration(Period.ofDays(Gen.integer(1..365).first()))
				.auctionLoanParams(createLoanAuctionParams(amount))
				.offers(Set.of(createOfferWithAmount(amount, Gen.long.first())))
				.status(Auction.AuctionStatus.ACTIVE_COMPLETE)
				.build()
	}

	private static AuctionLoanParams createLoanAuctionParams(int amount) {
		AuctionLoanParams.builder()
				.loanAmount(new Money(amount))
				.loanDuration(Period.ofMonths(Gen.integer(1..36).first()))
				.loanRate(Rate.fromPercentValue(Gen.integer(0, 100).first()))
				.build()
	}


	static Offer createOfferWithLenderNameAuctionId(String lenderName, Long auctionId){
		Offer.builder()
				.auctionId(auctionId)
				.lenderName(lenderName)
				.amount(new Money(Gen.double.first()))
				.rate(Rate.fromPercentValue(Gen.integer(0, 100).first()))
				.build()
	}
	static Offer createOffer(Long auctionId, double amount, double rate) {
		Offer.builder()
				.id(auctionId)
				.amount(new Money(amount))
				.rate(Rate.fromPercentValue(rate))
				.build()
	}

	static Offer createOfferWithAmount(int amount, long id){
		Offer.builder()
				.auctionId(id)
				.lenderName(Gen.string(20).first())
				.amount(new Money(amount))
				.rate(Rate.fromPercentValue(Gen.integer(0, 100).first()))
				.build()
	}

	static Auction createAuctionWithAmount(Integer amount) {
		createAuction(Gen.string(30).first(),
				Period.ofDays(Gen.integer.first()),
				amount,
				Period.ofDays(Gen.integer.first()),
				Gen.getDouble().first(),
				Gen.integer.first())
	}

	static Auction createAuctionWithUserNameAmount(String borrower, Integer amount) {
		createAuction(borrower,
				Period.ofDays(Gen.integer.first()),
				amount,
				Period.ofDays(Gen.integer.first()),
				Gen.getDouble().first(),
				Gen.integer.first())
	}

	static EndAuctionEvent createEndAuctionEvent(){
		EndAuctionEvent.builder()
			.offers(Set.of(createOfferWithAmount(Gen.integer.first(), Gen.long.first())))
			.auctionLoanParams(createLoanAuctionParams(Gen.integer.first()))
			.BorrowerUserName(Gen.string(20).first())
			.build()
	}

	static Auction createAuction(String borrowerName,
	                              Period auctionDuration,
	                              int amount,
	                              Period loanDuration,
	                              double rate,
	                              int rating,
								  Instant now) {
		Auction.builder()
				.borrower(createBorrower(borrowerName, rating))
				.endDate(now)
				.auctionDuration(auctionDuration)
				.auctionLoanParams(AuctionLoanParams.builder()
						.loanAmount(new Money(amount))
						.loanDuration(loanDuration)
						.loanRate(Rate.fromPercentValue(rate))
						.build())
				.status(Auction.AuctionStatus.ACTIVE)
				.build()
	}

	static Borrower createBorrower(String userName, double rating){
			Borrower.builder()
					.rating(new Rating(rating, Collections.emptyList()))
					.userDetails(UserDetails.builder()
							.name(Gen.string(20).first())
							.email(Gen.string(20).first())
							.userName(userName)
							.privateAccountNumber(UUID.randomUUID())
							.build())
					.build()

	}
}
