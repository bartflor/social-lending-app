package pl.fintech.solidlending.solidlendigplatform.domain.loan

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan
import spock.genesis.Gen

import java.time.LocalDate
import java.time.Period

class LoanDomainFactory {

	static Loan crateLoan(long id) {
		Loan.builder()
				.id(id)
				.borrowerUserName(Gen.string(20).first())
				.investments(Collections.emptySet())
				.amount(new Money(Gen.double.first()))
				.averageRate(Rate.fromPercentDouble(Gen.integer(0, 100).first()))
				.repayment(new Money(Gen.double.first()))
				.duration(Period.ofMonths(Gen.integer(0, 36).first()))
				.startDate(LocalDate.ofYearDay(Gen.integer(1900..2020).first(), Gen.integer(1..365).first()))
				.schedule(new RepaymentSchedule())
				.build()
	}

	static Investment createInvestment() {
		Investment.builder()
				.investmentId(Gen.long.first())
				.loanId(Gen.long.first())
				.lenderName(Gen.string(20).first())
				.value(new Money(Gen.double.first()))
				.rate(Rate.fromPercentDouble(Gen.integer(0, 100).first()))
				.duration(Period.ofMonths(Gen.integer(0, 36).first()))
				.schedule(new RepaymentSchedule())
				.build()
	}
}
