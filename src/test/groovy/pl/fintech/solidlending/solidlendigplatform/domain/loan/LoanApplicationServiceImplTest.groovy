package pl.fintech.solidlending.solidlendigplatform.domain.loan

import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionsTestsHelper
import pl.fintech.solidlending.solidlendigplatform.domain.common.TimeService
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.TransferOrderEvent
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.RepaymentNotExecuted
import pl.fintech.solidlending.solidlendigplatform.domain.payment.PaymentService
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class LoanApplicationServiceImplTest extends Specification {

	def loanDomainSvcMock = Mock(LoanDomainService)
	def transferSvcMock = Mock(PaymentService)
	def timeSvcMock = Mock(TimeService)
	@Subject
	def loanAppSvc = new LoanApplicationServiceImpl(loanDomainSvcMock, transferSvcMock, timeSvcMock)

	def "createLoan should crate new loan from given endAuctionEvent"(){
		given:
			def loanStartDate = Instant.ofEpochMilli(Gen.long.first())
			def event = AuctionsTestsHelper.createEndAuctionEvent()
			def investParams = NewInvestmentParams.builder()
					.LenderUserName(event.getOfferParamsSet().first().getLenderName())
					.BorrowerName(event.getBorrowerUserName())
					.investedMoney(event.getOfferParamsSet().first().getAmount())
					.returnRate(event.getOfferParamsSet().first().getRate())
					.investmentDuration(event.getLoanDuration())
					.investmentStartDate(loanStartDate)
					.build()
			def loanParams = NewLoanParams.builder()
					.borrowerUserName(event.getBorrowerUserName())
					.investmentsParams(List.of(investParams))
					.loanAmount(event.getLoanAmount())
					.loanStartDate(loanStartDate)
					.loanDuration(event.getLoanDuration())
					.build()
		when:
			loanAppSvc.createLoan(event)
		then:
			1*timeSvcMock.now() >> loanStartDate
			1*loanDomainSvcMock.createLoan(loanParams)
	}

	def "activateLoan should call domainSvc method with given id and call \
		execute on transferService with proper transferOrderEventsList"(){
		given:
			def randId = Gen.integer.first()
			def loan = LoanDomainFactory.crateLoan(randId)
			def transferOrderEventsList = List.of(TransferOrderEvent.builder()
					.targetUserName(loan.getBorrowerUserName())
					.sourceUserName(loan.getInvestments().first().getLenderName())
					.amount(loan.getInvestments().first().getLoanAmount())
					.build())
		when:
			loanAppSvc.activateLoan(randId)
		then:
			1*loanDomainSvcMock.findLoanById(randId) >> loan
			1*loanDomainSvcMock.activateLoan(randId)
		and:
			1*transferSvcMock.execute(transferOrderEventsList)
	}

	def "repayLoan should order transfer execution, and call reportRepayment gor loan with given id"(){
		given:
			def randId = Gen.integer.first()
			def loan = LoanDomainFactory.crateActiveLoan(randId)
			def transferOrderEvent= TransferOrderEvent.builder()
					.targetUserName(loan.getInvestments().first().getLenderName())
					.sourceUserName(loan.getBorrowerUserName())
					.amount(loan.getSchedule().getSchedule().first().getValue())
					.build()
		when:
			loanAppSvc.repayLoan(randId)
		then:
			1*loanDomainSvcMock.findLoanById(randId) >> loan
			1*loanDomainSvcMock.reportRepayment(randId)
		and:
			1*transferSvcMock.execute(transferOrderEvent)
	}

	def "repayLoan should throw exception given loan with no ACTIVE status"(){
		given:
			def randId = Gen.integer.first()
			def loan = LoanDomainFactory.crateLoan(randId)
		when:
			loanAppSvc.repayLoan(randId)
		then:
			1*loanDomainSvcMock.findLoanById(randId) >> loan
		and:
			def exception = thrown(RepaymentNotExecuted)
			exception.getMessage() == "Can not repay not ACTIVE loan with id: "+randId
	}

	def "repayLoan should throw exception given loan with paid all repayments in schedule"(){
		given:
			def randId = Gen.integer.first()
			def loan = LoanDomainFactory.crateActiveLoan(randId)
			def schedule = new RepaymentSchedule()
			schedule.addRepayment(Repayment.builder()
					.value(new Money(Gen.double.first()))
					.status(Repayment.Status.PAID)
					.build())
			loan.setSchedule(schedule)
		when:
			loanAppSvc.repayLoan(randId)
		then:
			1*loanDomainSvcMock.findLoanById(randId) >> loan
		and:
			def exception = thrown(RepaymentNotExecuted)
			exception.getMessage() == "No repayment left in schedule. Loan with id: "+randId+" is repaid"
	}
}
