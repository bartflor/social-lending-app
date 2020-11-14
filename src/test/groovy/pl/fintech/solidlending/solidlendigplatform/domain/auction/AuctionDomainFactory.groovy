package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.common.EndAuctionEvent
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating
import spock.genesis.Gen

import java.time.Instant
import java.time.LocalDate
import java.time.Period

class AuctionDomainFactory {
	static Auction createAuction(String borrowerName,
	                             Period auctionDuration,
	                             int amount,
	                             Period loanDuration,
	                             double rate,
	                             Instant loanStart,
	                             int rating) {
		Auction.builder()
				.borrowerUserName(borrowerName)
				.startDate(Instant.now())
				.auctionDuration(auctionDuration)
				.borrowerRating(new Rating(rating))
				.auctionLoanParams(AuctionLoanParams.builder()
						.loanStartDate(loanStart)
						.loanAmount(new Money(amount))
						.loanDuration(loanDuration)
						.loanRate(Rate.fromPercentDouble(rate))
						.build())
				.status(Auction.AuctionStatus.ACTIVE)
				.build()
	}

	static Auction createAuction(long id) {
		Auction.builder()
				.id(id)
				.borrowerUserName(Gen.string(20).first())
				.startDate(Gen.date.first().toInstant())
				.auctionDuration(Period.ofDays(Gen.integer(1..365).first()))
				.borrowerRating(new Rating(Gen.integer.first()))
				.auctionLoanParams(AuctionLoanParams.builder()
						.loanStartDate(Gen.date.first().toInstant())
						.loanAmount(new Money(Gen.double.first()))
						.loanDuration(Period.ofMonths(Gen.integer(1..36).first()))
						.loanRate(Rate.fromPercentDouble(Gen.integer(0..100).first()))
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
				Gen.date.first().toInstant(),
				Gen.integer.first())
	}

	static Auction createActiveCompleteAuction(long id) {
		def amount = Gen.integer.first();
		Auction.builder()
				.id(id)
				.borrowerUserName(Gen.string(20).first())
				.startDate(Gen.date.first().toInstant())
				.auctionDuration(Period.ofDays(Gen.integer(1..365).first()))
				.borrowerRating(new Rating(Gen.integer.first()))
				.auctionLoanParams(createLoanAuctionParams(amount))
				.offers(Set.of(createOfferWithAmount(amount, Gen.long.first())))
				.status(Auction.AuctionStatus.ACTIVE_COMPLETE)
				.build()
	}

	private static AuctionLoanParams createLoanAuctionParams(int amount) {
		AuctionLoanParams.builder()
				.loanStartDate(Gen.date.first().toInstant())
				.loanAmount(new Money(amount))
				.loanDuration(Period.ofMonths(Gen.integer(1..36).first()))
				.loanRate(Rate.fromPercentDouble(Gen.integer(0, 100).first()))
				.build()
	}


	static Offer createOfferWithLenderNameAuctionId(String lenderName, Long auctionId){
		Offer.builder()
				.auctionId(auctionId)
				.lenderName(lenderName)
				.amount(new Money(Gen.double.first()))
				.rate(Rate.fromPercentDouble(Gen.integer(0, 100).first()))
				.build()
	}

	static Offer createOfferWithAmount(int amount, long id){
		Offer.builder()
				.auctionId(id)
				.lenderName(Gen.string.first())
				.amount(new Money(amount))
				.rate(Rate.fromPercentDouble(Gen.integer(0, 100).first()))
				.build()
	}

	static Auction createAuctionWithAmount(Integer amount) {
		createAuction(Gen.string.first(),
				Period.ofDays(Gen.integer.first()),
				amount,
				Period.ofDays(Gen.integer.first()),
				Gen.getDouble().first(),
				Gen.date.first().toInstant(),
				Gen.integer.first())
	}

	static EndAuctionEvent createEndAuctionEvent(){
		EndAuctionEvent.builder()
			.offers(Collections.emptySet())
			.auctionLoanParams(createLoanAuctionParams(Gen.integer.first()))
			.BorrowerUserName(Gen.string(20).first())
			.build()
	}

	static Auction createAuction(String borrowerName,
	                              Period auctionDuration,
	                              int amount,
	                              Period loanDuration,
	                              double rate,
	                              Instant loanStart,
	                              int rating,
								  Instant now) {
		Auction.builder()
				.borrowerUserName(borrowerName)
				.startDate(now)
				.auctionDuration(auctionDuration)
				.borrowerRating(new Rating(rating))
				.auctionLoanParams(AuctionLoanParams.builder()
						.loanStartDate(loanStart)
						.loanAmount(new Money(amount))
						.loanDuration(loanDuration)
						.loanRate(Rate.fromPercentDouble(rate))
						.build())
				.status(Auction.AuctionStatus.ACTIVE)
				.build()
	}
}
