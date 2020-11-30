package pl.fintech.solidlending.solidlendigplatform.domain.loan

import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionsTestsHelper
import pl.fintech.solidlending.solidlendigplatform.domain.common.TimeService
import pl.fintech.solidlending.solidlendigplatform.domain.common.events.TransferOrderEvent
import pl.fintech.solidlending.solidlendigplatform.domain.payment.PaymentApplicationService
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class LoanApplicationServiceUT extends Specification {

	def loanDomainSvcMock = Mock(LoanDomainService)
	def paymentSvcMock = Mock(PaymentApplicationService)
	def timeSvcMock = Mock(TimeService)
	@Subject
	def loanAppSvc = new LoanApplicationServiceImpl(loanDomainSvcMock, paymentSvcMock, timeSvcMock)

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
			def loan = LoanTestHelper.crateLoan(randId)
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
			1*paymentSvcMock.execute(transferOrderEventsList)
	}

	def "repayLoan should order transfer execution, and call reportRepayment gor loan with given id"(){
		given:
			def randId = Gen.integer.first()
			def investment = LoanTestHelper.createInvestment()
			def transferOrderEvent= TransferOrderEvent.builder()
					.targetUserName(investment.getLenderName())
					.sourceUserName(investment.getBorrowerName())
					.amount(investment.getSchedule().getSchedule().first().getValue())
					.build()
		when:
			loanAppSvc.repayLoan(randId)
		then:
			1*loanDomainSvcMock.getLoanInvestmentsForRepayment(randId) >> Set.of(investment)
			1*loanDomainSvcMock.reportRepayment(randId)
		and:
			1*paymentSvcMock.execute(transferOrderEvent)
	}

}
