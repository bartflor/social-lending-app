package pl.fintech.solidlending.solidlendigplatform.domain.loan

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.RepaymentException
import spock.genesis.Gen
import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit

class RepaymentScheduleUT extends Specification {

	def "findNextRepayment should return repayment with the earliest date and EXPECTED status"(){
		given:
			def startDate = Instant.ofEpochMilli(Gen.long.first())
			def repayment = new Money(Gen.integer(0, Integer.MAX_VALUE).first() as double)
			def schedule = new RepaymentSchedule()
			def expectedResponse = new Repayment(startDate.plus(60, ChronoUnit.DAYS),
					repayment, Repayment.Status.EXPECTED)
			schedule.addRepayment(new Repayment(startDate, repayment, Repayment.Status.PAID))
			schedule.addRepayment(new Repayment(startDate.plus(30, ChronoUnit.DAYS),
					repayment, Repayment.Status.PAID))
			schedule.addRepayment(expectedResponse)
			schedule.addRepayment(new Repayment(startDate.plus(90, ChronoUnit.DAYS),
					repayment, Repayment.Status.EXPECTED))
		expect:
			schedule.getNextRepayment() == expectedResponse
	}

	def "findNextRepayment should return repayment with the earliest date and LATE status"(){
		given:
			def startDate = Instant.ofEpochMilli(Gen.long.first())
			def repayment = new Money(Gen.integer(0, Integer.MAX_VALUE).first() as double)
			def schedule = new RepaymentSchedule()
			def expectedResponse = new Repayment(startDate.plus(20, ChronoUnit.DAYS),
					repayment, Repayment.Status.LATE)
			schedule.addRepayment(new Repayment(startDate, repayment, Repayment.Status.PAID))
			schedule.addRepayment(expectedResponse)
			schedule.addRepayment(new Repayment(startDate.plus(30, ChronoUnit.DAYS),
					repayment, Repayment.Status.LATE))
			schedule.addRepayment(new Repayment(startDate.plus(90, ChronoUnit.DAYS),
					repayment, Repayment.Status.EXPECTED))
		expect:
			schedule.getNextRepayment() == expectedResponse
	}

	def "findNextRepayment should return empty Optional when all repayments have PAID status"(){
		given:
			def id = Gen.integer.first()
			def startDate = Instant.ofEpochMilli(Gen.long.first())
			def repayment = new Money(Gen.integer(0, Integer.MAX_VALUE).first() as double)
			def schedule = new RepaymentSchedule()
			schedule.setType(RepaymentSchedule.Type.LOAN)
			schedule.setOwnerId(id)
			schedule.addRepayment(new Repayment(startDate, repayment, Repayment.Status.PAID))
			schedule.addRepayment(new Repayment(startDate.plus(30, ChronoUnit.DAYS),
					repayment, Repayment.Status.PAID))
			schedule.addRepayment(new Repayment(startDate.plus(90, ChronoUnit.DAYS),
					repayment, Repayment.Status.PAID))
		when:
			schedule.getNextRepayment()
		then:
			def ex = thrown(RepaymentException)
			ex.getMessage() == "No repayment left in schedule. LOAN with id: "+id+" is repaid"
	}

	def "hasPaidAllScheduledRepayment should true when all repayments have PAID status"(){
		given:
			def startDate = Instant.ofEpochMilli(Gen.long.first())
			def repayment = new Money(Gen.integer(0, Integer.MAX_VALUE).first() as double)
			def paidSchedule = new RepaymentSchedule()
			paidSchedule.addRepayment(new Repayment(startDate, repayment, Repayment.Status.PAID))
			paidSchedule.addRepayment(new Repayment(startDate.plus(30, ChronoUnit.DAYS),
					repayment, Repayment.Status.PAID))
			def unpaidSchedule = new RepaymentSchedule()
			paidSchedule.addRepayment(new Repayment(startDate, repayment, Repayment.Status.PAID))
			paidSchedule.addRepayment(new Repayment(startDate.plus(30, ChronoUnit.DAYS),
					repayment, Repayment.Status.PAID))
			unpaidSchedule.addRepayment(new Repayment(startDate, repayment, Repayment.Status.PAID))
			unpaidSchedule.addRepayment(new Repayment(startDate.plus(90, ChronoUnit.DAYS),
					repayment, Repayment.Status.EXPECTED))
		expect:
			paidSchedule.hasPaidAllScheduledRepayment()
		and:
			!unpaidSchedule.hasPaidAllScheduledRepayment()
	}

	def "reportRepayment should update last repayment with EXPECTED status"(){
		given:
			def startDate = Instant.ofEpochMilli(Gen.long.first())
			def repayment = new Money(Gen.integer(0, Integer.MAX_VALUE).first() as double)
			def schedule = new RepaymentSchedule()
			def shouldChangeStatus = new Repayment(startDate.plus(60, ChronoUnit.DAYS),
					repayment, Repayment.Status.EXPECTED)
			schedule.addRepayment(new Repayment(startDate, repayment, Repayment.Status.PAID))
			schedule.addRepayment(new Repayment(startDate.plus(30, ChronoUnit.DAYS),
					repayment, Repayment.Status.PAID))
			schedule.addRepayment(shouldChangeStatus)
		when:
			schedule.reportRepayment()
		then:
			shouldChangeStatus.getStatus() == Repayment.Status.PAID
	}

	def "reportRepayment should throw exception when no  repayment with EXPECTED status found"(){
		given:
			def id = Gen.integer.first()
			def startDate = Instant.ofEpochMilli(Gen.long.first())
			def repayment = new Money(Gen.integer(0, Integer.MAX_VALUE).first() as double)
			def schedule = new RepaymentSchedule()
			schedule.setType(RepaymentSchedule.Type.LOAN)
			schedule.setOwnerId(id)
			schedule.addRepayment(new Repayment(startDate, repayment, Repayment.Status.PAID))
			schedule.addRepayment(new Repayment(startDate.plus(30, ChronoUnit.DAYS),
					repayment, Repayment.Status.PAID))
		when:
			schedule.reportRepayment()
		then:
			def exception = thrown(RepaymentException)
			exception.getMessage() == "No repayment left in schedule. LOAN with id: "+id+" is repaid"
	}
}
