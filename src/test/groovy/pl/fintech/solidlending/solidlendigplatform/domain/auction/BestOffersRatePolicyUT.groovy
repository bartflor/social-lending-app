package pl.fintech.solidlending.solidlendigplatform.domain.auction

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class BestOffersRatePolicyUT extends Specification {

	@Subject
	def bestOffersRatePolicy = new BestOffersRatePolicy()

	def 'selectOffers should return set of offers with lowest Rate'() {
		given:
			def loanAmount = new Money(100)
			def offer1 = AuctionsTestsHelper.createOffer(Gen.long.first(), 50, 5)
			def offer2 = AuctionsTestsHelper.createOffer(Gen.long.first(), 50, 15)
			def offer3 = AuctionsTestsHelper.createOffer(Gen.long.first(), 50, 10)
		when:
			def result = bestOffersRatePolicy.selectOffers(Set.of(offer1, offer2, offer3), loanAmount)
		then:
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(50) &&
					offer.getRate() == Rate.fromPercentValue(5) })
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(50) &&
					 offer.getRate() == Rate.fromPercentValue(10) })
	}

	def 'selectOffers should return set of offers with lowest rate and reduce amount of offer with bigger rate \
		to mach requested loan amount.'() {
		given:

			def loanAmount = new Money(100)
			def offer1 = AuctionsTestsHelper.createOffer(Gen.long.first(), 50, 5)
			def offer2 = AuctionsTestsHelper.createOffer(Gen.long.first(), 50, 15)
			def offer3 = AuctionsTestsHelper.createOffer(Gen.long.first(), 80, 10)
		when:
			def result = bestOffersRatePolicy.selectOffers(Set.of(offer1, offer2, offer3), loanAmount)
		then:
			result.size() == 2
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(50) &&
					offer.getRate() == Rate.fromPercentValue(5) })
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(50) &&
					offer.getRate() == Rate.fromPercentValue(10) })

	}

	def 'selectOffers should return set of one offer with lowest rate'() {
		given:

			def loanAmount = new Money(100)
			def offer1 = AuctionsTestsHelper.createOffer(Gen.long.first(), 100, 3)
			def offer2 = AuctionsTestsHelper.createOffer(Gen.long.first(), 70, 5)
			def offer3 = AuctionsTestsHelper.createOffer(Gen.long.first(), 70, 15)

		when:
			def result = bestOffersRatePolicy.selectOffers(Set.of(offer1, offer2, offer3), loanAmount)
		then:
			result.size() == 1
			result.stream().anyMatch({ offer -> offer.getAmount() == new Money(100) &&
					offer.getRate() == Rate.fromPercentValue(3) })

	}

	def 'selectOffers should return set of offers with reduced first added offer'() {
		given:
			def loanAmount = new Money(100)
			def offer1 = AuctionsTestsHelper.createOffer(1, 70, 15)
			def offer2 = AuctionsTestsHelper.createOffer(2, 70, 10)
		when:
			def result = bestOffersRatePolicy.selectOffers(Set.of(offer1, offer2), loanAmount)
		then:
			result.size() == 2
			result.stream().anyMatch({ offer -> offer.getId() == 1 &&
					offer.getAmount() == new Money(30) &&
					offer.getRate() == Rate.fromPercentValue(15) })
			result.stream().anyMatch({ offer ->  offer.getId() == 2 &&
					offer.getAmount() == new Money(70) &&
					offer.getRate() == Rate.fromPercentValue(10) })
	}

	def 'selectOffers should return set of offers containing all offers and reduce amount of last added offer, \
		given same offers'() {
		given:
			def loanAmount = new Money(100)
			def offer1 = AuctionsTestsHelper.createOffer(1, 40, 15)
			def offer2 = AuctionsTestsHelper.createOffer(2, 40, 15)
			def offer3 = AuctionsTestsHelper.createOffer(3, 40, 15)
		when:
			def result = bestOffersRatePolicy.selectOffers(Set.of(offer1, offer2, offer3), loanAmount)
		then:
			result.size() == 3
			result.stream().anyMatch({ offer -> offer.getId() == 1 &&
					offer.getAmount() == new Money(40) &&
					offer.getRate() == Rate.fromPercentValue(15) })
			result.stream().anyMatch({ offer -> offer.getId() == 2 &&
					offer.getAmount() == new Money(40) &&
					offer.getRate() == Rate.fromPercentValue(15) })
			result.stream().anyMatch({ offer -> offer.getId() == 3 &&
					offer.getAmount() == new Money(20) &&
					offer.getRate() == Rate.fromPercentValue(15) })
	}
}
