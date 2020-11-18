package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import pl.fintech.solidlending.solidlendigplatform.domain.common.TransferOrderEvent
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.loan.InvestmentRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanDomainFactory
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Repayment
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentSchedule
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentScheduleRepository
import pl.fintech.solidlending.solidlendigplatform.domain.payment.TransferService
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.AddStubRepositoriesToContext
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.MockTransferService
import spock.genesis.Gen
import spock.lang.Ignore
import spock.lang.Specification

@Import([AddStubRepositoriesToContext, MockTransferService])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanControllerIntegrationTest extends Specification{

	@LocalServerPort
	int randomPort
	@Autowired
	TransferService transferSvcMock
	@Autowired
	LoanRepository loanRepository
	@Autowired
	RepaymentScheduleRepository scheduleRepository
	@Autowired
	InvestmentRepository investmentRepository
	RequestSpecification restClient



	def setup() {
		restClient = RestAssured.given()
				.port(randomPort)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.log().all()
	}

	def "GET /api/loans/{loanId}/repay should call transferService with proper PaymentOrderEvent \
		 and update loan repayment schedule in repository"(){
		given:
			def repaymentsNumber = Gen.integer(1,10).first()
			def totalLoanValue = Gen.integer(100, Integer.MAX_VALUE).first()
			def investmentSchedule = LoanDomainFactory.createRepaymentSchedule(repaymentsNumber, totalLoanValue)
			def loanSchedule = LoanDomainFactory.createRepaymentSchedule(repaymentsNumber, totalLoanValue)

			def investment = LoanDomainFactory.createInvestmentWithSchedule(investmentSchedule)
			def loan = LoanDomainFactory.crateActiveLoanMatchingInvestment(investment, loanSchedule)
			def loanId = loanRepository.save(loan)
			loanSchedule = loan.getSchedule()
			loanSchedule.setOwnerId(loanId)
			loanSchedule.setId(1)
			def investmentId = investmentRepository.save(investment)
			investmentSchedule = investment.getSchedule()
			investmentSchedule.setOwnerId(investmentId)
			investmentSchedule.setId(2)
			scheduleRepository.save(loanSchedule)
			scheduleRepository.save(investmentSchedule)
			def expectedTransferOrder = TransferOrderEvent.builder()
					.amount(new Money(totalLoanValue/repaymentsNumber))
					.sourceUserName(loan.getBorrowerUserName())
					.targetUserName(investment.getLenderName())
					.build()
		when:
			def response = restClient.get("/api/loans/"+loanId+"/repay")
		then:
			response.statusCode() == 201
			1 * transferSvcMock.execute(expectedTransferOrder)
		and:
			def fromRepoInvestmentSchedule = scheduleRepository.findRepaymentScheduleByInvestmentId(investmentId).get()
			fromRepoInvestmentSchedule.getSchedule().stream()
					.filter({ repayment -> repayment.getStatus() == (Repayment.Status.PAID) })
					.count() == 1
		and:
			def fromRepoLoanSchedule = scheduleRepository.findRepaymentScheduleByLoanId(loanId).get()
			fromRepoLoanSchedule.getSchedule().stream()
					.filter({ repayment -> repayment.getStatus() == (Repayment.Status.PAID) })
					.count() == 1
	}



}
