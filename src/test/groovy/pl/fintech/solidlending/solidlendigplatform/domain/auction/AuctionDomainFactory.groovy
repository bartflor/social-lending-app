package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating
import spock.genesis.Gen

import java.time.LocalDate
import java.time.Period

class AuctionDomainFactory {
	static Auction createAuction(String borrowerName,
	                             Period auctionDuration,
	                             int amount,
	                             Period loanDuration,
	                             double rate,
	                             LocalDate loanStart,
	                             int rating) {
		Auction.builder()
				.borrowerUserName(borrowerName)
				.startDate(LocalDate.now())
				.auctionDuration(auctionDuration)
				.borrowerRating(new Rating(rating))
				.auctionLoanParams(AuctionLoanParams.builder()
						.loanStartDate(loanStart)
						.loanAmount(new Money(amount))
						.loanDuration(loanDuration)
						.loanRate(new Rate(rate))
						.build())
				.status(Auction.AuctionStatus.ACTIVE)
				.build()
	}

	static Auction createAuction(long id) {
		Auction.builder()
				.id(id)
				.borrowerUserName(Gen.string(20).first())
				.startDate(LocalDate.now())
				.auctionDuration(Period.ofDays(Gen.integer(1..365).first()))
				.borrowerRating(new Rating(Gen.integer.first()))
				.auctionLoanParams(AuctionLoanParams.builder()
						.loanStartDate(LocalDate.now())
						.loanAmount(new Money(Gen.double.first()))
						.loanDuration(Period.ofMonths(Gen.integer(1..36).first()))
						.loanRate(new Rate(Gen.double.first()))
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
				LocalDate.ofYearDay(Gen.integer(1900..2020).first(), Gen.integer(1..365).first()),
				Gen.integer.first())
	}

	static Offer createOfferWithLenderNameAuctionId(String lenderName, Long auctionId){
		Offer.builder()
				.auctionId(auctionId)
				.lenderName(lenderName)
				.amount(new Money(Gen.double.first()))
				.rate(new Rate(Gen.double.first()))
				.build()
	}

	static Offer createOfferWithAmount(int amount, long id){
		Offer.builder()
				.auctionId(id)
				.lenderName(Gen.string.first())
				.amount(new Money(amount))
				.rate(new Rate(Gen.double.first()))
				.build()
	}

	static Auction createAuctionWithAmount(Integer amount) {
		createAuction(Gen.string.first(),
				Period.ofDays(Gen.integer.first()),
				amount,
				Period.ofDays(Gen.integer.first()),
				Gen.getDouble().first(),
				LocalDate.ofYearDay(Gen.integer(1900..2020).first(), Gen.integer(1..365).first()),
				Gen.integer.first())
	}

}
