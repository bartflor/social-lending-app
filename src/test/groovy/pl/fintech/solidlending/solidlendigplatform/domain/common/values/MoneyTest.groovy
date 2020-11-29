package pl.fintech.solidlending.solidlendigplatform.domain.common.values

import spock.genesis.Gen
import spock.lang.Specification


class MoneyTest extends Specification {

	def "IsMoreOrEqual should return true, given two equal numbers"() {
		given:
			def val1 = Gen.getDouble().first()
			def money1 = new Money(val1)
			def money2 = new Money(val1)
		expect:
			money1.isMoreOrEqual(money2)
	}

	def "IsMoreOrEqual should return false, comparing lower number to bigger"() {
		given:
			def val1 = Gen.getDouble().first()
			def money1 = new Money(val1)
			def money2 = new Money(val1+1)
		expect:
			!money1.isMoreOrEqual(money2)
	}

	def "IsMoreOrEqual should return true, comparing bigger number to lower"() {
		given:
			def val1 = Gen.getDouble().first()
			def money1 = new Money(val1+1)
			def money2 = new Money(val1)
		expect:
			money1.isMoreOrEqual(money2)
	}

	def "isEqual should return false given different money amount"() {
		given:
			def val1 = Gen.getDouble().first()
			def money1 = new Money(val1)
			def money2 = new Money(val1+2)
		expect:
			!money2.isEqual(money1)
	}
}
