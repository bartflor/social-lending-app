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
				.loanParams(LoanParams.builder()
						.loanStartDate(loanStart)
						.loanAmount(new Money(amount))
						.loanDuration(loanDuration)
						.loanRate(new Rate(rate))
						.build())
				.status(Auction.AuctionStatus.ACTIVE)
				.build()
	}

	static Auction createAuction(String... borrowerName) {
		def name = borrowerName.length==0? Gen.string.iterator().next() : borrowerName[0]
		createAuction(name,
				Period.ofDays(Gen.integer.iterator().next()),
				Gen.integer.iterator().next(),
				Period.ofDays(Gen.integer.iterator().next()),
				Gen.getDouble().iterator().next(),
				LocalDate.ofYearDay(Gen.integer(1900..2020).iterator().next(), Gen.integer(1..365).iterator().next()),
				Gen.integer.iterator().next())
	}

	static Offer createOffer(String lenderName, Long auctionId){
		Offer.builder()
				.auctionId(auctionId)
				.lenderName(lenderName)
				.amount(new Money(Gen.integer.iterator().next()))
				.rate(new Rate(Gen.integer.iterator().next()))
				.duration(Period.of(Gen.integer(1900..2020).iterator().next(), Gen.integer(1..12).iterator().next(), Gen.integer(1..30).iterator().next()))
				.build()
	}
}
