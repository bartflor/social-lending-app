package pl.fintech.solidlending.solidlendigplatform.domain.common.values

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.exception.ValueNotAllowedException
import spock.genesis.Gen
import spock.lang.Specification

class RateTest extends Specification {

	def "FromPercentValue should throw exception given too big input"() {
		when:
			Rate.fromPercentValue(-1)
		then:
			thrown(ValueNotAllowedException)
	}

	def "FromPercentValue should throw exception given negative input"() {
		when:
			Rate.fromPercentValue(101)
		then:
			thrown(ValueNotAllowedException)
	}

	def "FromPercentValue rate object given proper input"() {
		when:
			def value = Gen.integer(1,99).first()
			def result = Rate.fromPercentValue(value)
		then:
			result.getPercentValue() == value
	}
}
