package pl.fintech.solidlending.solidlendigplatform.domain.loan

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan
import spock.genesis.Gen

import java.time.Period

class LoanDomainFactory {

	static Loan crateLoan(long randID) {
		null
	}

	static Investment createInvestment() {
		Investment.builder()
			.investmentId(Gen.long.first())
			.loanId(Gen.long.first())
			.lenderName(Gen.string(20).first())
			.value(new Money(Gen.double.first()))
			.rate(new Rate(Gen.double.first()))
			.duration(Period.ofMonths(Gen.integer(0, 36).first()))
			.schedule(new RepaymentSchedule())
			.build()
	}
}
