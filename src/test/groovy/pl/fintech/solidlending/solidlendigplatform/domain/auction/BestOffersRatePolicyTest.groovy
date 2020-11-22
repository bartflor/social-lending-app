package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class BestOffersRatePolicyTest extends Specification {

	@Subject
	def bestOffersRatePolicy = new BestOffersRatePolicy()

	def 'selectOffers should return set of offers with lowes Rate'() {
		given:
			def loanAmount = new Money(100)
			def loanParams = AuctionLoanParams.builder().loanAmount(loanAmount).build()
			def offer1 = Offer.builder()
					.amount(new Money(50))
					.rate(Rate.fromPercentValue(15))
					.build()
			def offer2 = Offer.builder()
					.amount(new Money(50))
					.rate(Rate.fromPercentValue(10))
					.build()
			def offer3 = Offer.builder()
					.amount(new Money(50))
					.rate(Rate.fromPercentValue(5))
					.build()
		when:
			def result = bestOffersRatePolicy.selectOffers(Set.of(offer1, offer2, offer3), loanParams)
		then:
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(50) &&
					offer.getRate() == Rate.fromPercentValue(5) })
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(50) &&
					 offer.getRate() == Rate.fromPercentValue(10) })
	}

	def 'selectOffers should return set of offers with lowest rate and reduce amount of rate with bigger rate \
		to mach requested loan amount.'() {
		given:

			def loanAmount = new Money(100)
			def loanParams = AuctionLoanParams.builder().loanAmount(loanAmount).build()
			def offer1 = Offer.builder()
					.amount(new Money(50))
					.rate(Rate.fromPercentValue(5))
					.build()
			def offer2 = Offer.builder()
					.amount(new Money(70))
					.rate(Rate.fromPercentValue(10))
					.build()
			def offer3 = Offer.builder()
					.amount(new Money(50))
					.rate(Rate.fromPercentValue(15))
					.build()
		when:
			def result = bestOffersRatePolicy.selectOffers(Set.of(offer1, offer2, offer3), loanParams)
		then:
			result.size() == 2
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(50) &&
					offer.getRate() == Rate.fromPercentValue(5) })
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(50) &&
					offer.getRate() == Rate.fromPercentValue(10) })

	}

	def 'selectOffers should return set of offers containing bigger rate offer with amount split allowed'() {
		given:

			def loanAmount = new Money(100)
			def loanParams = AuctionLoanParams.builder().loanAmount(loanAmount).build()
			def offer1 = Offer.builder()
					.amount(new Money(100))
					.rate(Rate.fromPercentValue(10))
					.build()
			def offer2 = Offer.builder()
					.amount(new Money(70))
					.rate(Rate.fromPercentValue(5))
					.build()
			def offer3 = Offer.builder()
					.amount(new Money(70))
					.rate(Rate.fromPercentValue(15))
					.build()
		when:
			def result = bestOffersRatePolicy.selectOffers(Set.of(offer1, offer2, offer3), loanParams)
		then:
			result.size() == 2
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(70) &&
					offer.getRate() == Rate.fromPercentValue(5) })
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(30) &&
					offer.getRate() == Rate.fromPercentValue(10) })
	}

	def 'selectOffers should return set of offers with divided second added offer'() {
		given:
			def loanAmount = new Money(100)
			def loanParams = AuctionLoanParams.builder().loanAmount(loanAmount).build()

			def offer1 = Offer.builder()
					.id(1)
					.amount(new Money(70))
					.rate(Rate.fromPercentValue(15))
					.build()
			def offer2 = Offer.builder()
					.id(2)
					.amount(new Money(70))
					.rate(Rate.fromPercentValue(15))
					.build()
		when:
			def result = bestOffersRatePolicy.selectOffers(Set.of(offer1, offer2), loanParams)
		then:
			result.size() == 2
			result.stream().anyMatch({ offer -> offer.getId() == 1 &&
					offer.getAmount() == new Money(30) &&
					offer.getRate() == Rate.fromPercentValue(15) })
			result.stream().anyMatch({ offer ->  offer.getId() == 2 &&
					offer.getAmount() == new Money(70) &&
					offer.getRate() == Rate.fromPercentValue(15) })
	}

	def 'selectOffers should return set of offers containing all offers and reduce amount of offer with biggest rate \
		to mach requested loan amount.\''() {
		given:
			def loanAmount = new Money(100)
			def loanParams = AuctionLoanParams.builder().loanAmount(loanAmount).build()
			def offer1 = Offer.builder()
					.id(Gen.long.first())
					.amount(new Money(40))
					.rate(Rate.fromPercentValue(15))
					.build()
			def offer2 = Offer.builder()
					.id(Gen.long.first())
					.amount(new Money(40))
					.rate(Rate.fromPercentValue(20))
					.build()
			def offer3 = Offer.builder()
					.id(Gen.long.first())
					.amount(new Money(40))
					.rate(Rate.fromPercentValue(15))
					.build()
		when:
			def result = bestOffersRatePolicy.selectOffers(Set.of(offer1, offer2, offer3), loanParams)
		then:
			result.size() == 3
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(40) &&
					offer.getRate() == Rate.fromPercentValue(15) })
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(20) &&
					offer.getRate() == Rate.fromPercentValue(20) })
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(40) &&
					offer.getRate() == Rate.fromPercentValue(15) })
	}
}
