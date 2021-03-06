package pl.fintech.solidlending.solidlendigplatform.domain.loan

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate

import spock.genesis.Gen

import java.time.Instant
import java.time.Period
import java.time.temporal.ChronoUnit

class LoanTestHelper {

	static Loan crateLoan(long id) {
		Loan.builder()
				.id(id)
				.borrowerUserName(Gen.string(20).first())
				.investments(Set.of(createInvestment()))
				.amount(new Money(Gen.double.first()))
				.averageRate(Rate.fromPercentValue(Gen.integer(0, 100).first()))
				.repayment(new Money(Gen.double.first()))
				.duration(Period.ofMonths(Gen.integer(0, 36).first()))
				.startDate(Instant.now())
				.schedule(new RepaymentSchedule())
				.build()
	}

	static Investment createInvestment() {
		Investment.builder()
				.investmentId(Gen.long.first())
				.loanId(Gen.long.first())
				.lenderName(Gen.string(20).first())
				.returnAmount(new Money(Gen.double.first()))
				.loanAmount(new Money(Gen.double.first()))
				.rate(Rate.fromPercentValue(Gen.integer(0, 100).first()))
				.duration(Period.ofMonths(Gen.integer(0, 36).first()))
				.schedule(createRepaymentSchedule(Gen.integer(2,10).first(), Gen.integer(10,100000).first()))
				.build()
	}

	static Loan crateActiveLoanMatchingInvestment(Investment investment, RepaymentSchedule schedule) {
		schedule.setType(RepaymentSchedule.Type.LOAN)
		Loan.builder()
				.borrowerUserName(Gen.string(20).first())
				.investments(Set.of(investment))
				.amount(investment.getReturnAmount())
				.averageRate(investment.getRate())
				.repayment(investment.getSchedule().getNextRepayment().getValue())
				.duration(investment.getDuration())
				.startDate(Instant.now())
				.schedule(schedule)
				.status(Loan.LoanStatus.ACTIVE)
				.build()
	}
	static Loan crateActiveLoan(long id) {
		def schedule = createRepaymentSchedule(Gen.integer(2,10).first(), Gen.integer(10,100000).first())
		Loan.builder()
				.id(id)
				.borrowerUserName(Gen.string(20).first())
				.investments(Set.of(createInvestmentWithSchedule(schedule)))
				.amount(new Money(Gen.double.first()))
				.averageRate(Rate.fromPercentValue(Gen.integer(0, 100).first()))
				.repayment(new Money(Gen.double.first()))
				.duration(Period.ofMonths(Gen.integer(0, 36).first()))
				.startDate(Instant.now())
				.schedule(schedule)
				.status(Loan.LoanStatus.ACTIVE)
				.build()
	}
	static Loan crateActiveEmptyLoan(long id) {
		def schedule = createRepaymentSchedule(Gen.integer(2,10).first(), Gen.integer(10,100000).first())
		Loan.builder()
				.id(id)
				.borrowerUserName(Gen.string(20).first())
				.investments(Collections.emptySet())
				.amount(new Money(Gen.double.first()))
				.averageRate(Rate.fromPercentValue(Gen.integer(0, 100).first()))
				.repayment(new Money(Gen.double.first()))
				.duration(Period.ofMonths(Gen.integer(0, 36).first()))
				.startDate(Instant.now())
				.schedule(schedule)
				.status(Loan.LoanStatus.ACTIVE)
				.build()
	}

	static Investment createInvestmentWithSchedule(RepaymentSchedule schedule){
		schedule.setType(RepaymentSchedule.Type.INVESTMENT)
		Investment.builder()
				.investmentId(Gen.long.first())
				.lenderName(Gen.string(20).first())
				.loanAmount(new Money(Gen.double.first()))
				.returnAmount(new Money(Gen.double.first()))
				.rate(Rate.fromPercentValue(Gen.integer(0, 100).first()))
				.duration(Period.ofMonths(Gen.integer(0, 36).first()))
				.schedule(schedule)
				.build()
	}

	static Investment createInvestmentWith(RepaymentSchedule schedule, double amount, double rate, int repayments){
		schedule.setType(RepaymentSchedule.Type.INVESTMENT)
		Investment.builder()
				.investmentId(Gen.long.first())
				.lenderName(Gen.string(20).first())
				.loanAmount(new Money(amount))
				.returnAmount(new Money(amount*(100+rate)/100))
				.rate(Rate.fromPercentValue(rate))
				.duration(Period.ofMonths(repayments))
				.schedule(schedule)
				.build()
	}

	static RepaymentSchedule createRepaymentSchedule(int repaymentsNumber, int totalLoanValue){
		def schedule = new RepaymentSchedule()
		def startDay = Instant.now()
		def singleRepaymentValue = totalLoanValue/repaymentsNumber
		for(int i = 1; i<=repaymentsNumber; i++) {
			def repayment = Repayment.builder()
					.value(new Money(singleRepaymentValue))
					.date(startDay.plus(i*30, ChronoUnit.DAYS))
					.build()
			schedule.addRepayment(repayment)
		}
		schedule
	}

	static RepaymentSchedule createRepaymentSchedule(int repaymentsNumber, int totalLoanValue, Instant startDay){
		def schedule = new RepaymentSchedule()
		def singleRepaymentValue = totalLoanValue/repaymentsNumber
		for(int i = 1; i<=repaymentsNumber; i++) {
			def repayment = Repayment.builder()
					.value(new Money(singleRepaymentValue))
					.date(startDay.plus(i*30, ChronoUnit.DAYS))
					.build()
			schedule.addRepayment(repayment)
		}
		schedule
	}
}
