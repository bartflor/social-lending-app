package pl.fintech.solidlending.solidlendigplatform.domain.loan


import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.exception.ValueNotAllowedException
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant
import java.time.Period

class LoanFactoryTest extends Specification {
	@Subject
	def loanFactory = new LoanFactory()

	def "createLoan should return Loan object with proper schedule from given newLoanParams"(){
		given:
			def borrowerName = Gen.string(20).first()
			def repaymentsNumber = 5
			def investment1value = 100
			def startDate = Instant.ofEpochMilli(Gen.long.first())
			def schedule1 =	LoanDomainFactory.createRepaymentSchedule(repaymentsNumber, investment1value, startDate)
			def investment1 = LoanDomainFactory.createInvestmentWithSchedule(schedule1)
			def rate1Value = 10
			investment1.setRate(Rate.fromPercentValue(rate1Value))
			def investment2value = 200
			def schedule2 =	LoanDomainFactory.createRepaymentSchedule(repaymentsNumber,investment2value, startDate)
			def investment2 = LoanDomainFactory.createInvestmentWithSchedule(schedule2)
			def rate2Value = 10
			investment2.setRate(Rate.fromPercentValue(rate2Value))
			def loanDurationMonths = Period.ofMonths(repaymentsNumber)
			def newLoanParams = NewLoanParams.builder()
					.borrowerUserName(borrowerName)
					.loanAmount(new Money(investment1value+investment2value))
					.loanStartDate(startDate)
					.loanDuration(loanDurationMonths)
					.build()

		when:
			def loan = loanFactory.createLoan(newLoanParams, Set.of(investment1, investment2))
		then:
			loan.getBorrowerUserName() == borrowerName
			loan.getInvestments().size() == 2
			loan.getInvestments().contains(investment1)
			loan.getInvestments().contains(investment2)
			loan.getStatus() == Loan.LoanStatus.UNCONFIRMED
			loan.getDuration() == loanDurationMonths
			loan.getAverageRate() == Rate.fromPercentValue((rate1Value+rate2Value)/2)
			loan.getStartDate() == startDate
		and:
			def schedule = loan.getSchedule()
			def repaymentsList = schedule.getSchedule()
			repaymentsList.size() == 5
			repaymentsList.first().getValue() == new Money((investment1value+investment2value)/repaymentsNumber)
	}

	def "createLoan throws exception given empty investments set"(){
		when:
			loanFactory.createLoan(NewLoanParams.builder().build(), Collections.emptySet())
		then:
			def exception = thrown(ValueNotAllowedException)
			exception.getMessage() == "Empty investment set not allowed. Can not calculate average rate from empty investment set."

	}
}
