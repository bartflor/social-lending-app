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
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanDomainFactory
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Repayment
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentScheduleRepository
import pl.fintech.solidlending.solidlendigplatform.domain.payment.PaymentService
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.MockPaymentService
import pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config.PostgresContainerTestSpecification
import spock.genesis.Gen

@Import([MockPaymentService])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanControllerFT extends PostgresContainerTestSpecification{

	@LocalServerPort
	int randomPort
	@Autowired
	PaymentService paymentSvcMock
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
			def loan = LoanDomainFactory.crateActiveEmptyLoan(repaymentsNumber)
//			def loan = LoanDomainFactory.crateActiveLoanMatchingInvestment(investment, loanSchedule)
			def loanId = loanRepository.save(loan)
			loanSchedule.setOwnerId(loanId)
			loanSchedule.setId(1)
			def investmentId = investmentRepository.save(investment)
			investmentSchedule = investment.getSchedule()
			investmentSchedule.setOwnerId(investmentId)
			investmentSchedule.setId(2)
			scheduleRepository.save(loanSchedule)
			scheduleRepository.save(investmentSchedule)
			loan.setInvestments(Set.of(investment))
			loan.setSchedule(loanSchedule)
			loanRepository.update(loanId, loan)
			def expectedTransferOrder = TransferOrderEvent.builder()
					.amount(new Money(totalLoanValue/repaymentsNumber))
					.sourceUserName(loan.getBorrowerUserName())
					.targetUserName(investment.getLenderName())
					.build()
		when:
			def response = restClient.get("/api/loans/"+loanId+"/repay")
		then:
			response.statusCode() == 201
			1 * paymentSvcMock.execute(_) >> {arg -> arg == expectedTransferOrder}
		and:
			def fromRepoInvestmentSchedule = investmentRepository.findAllByUsername(investment.getLenderName())
					.first().getSchedule()
			fromRepoInvestmentSchedule.getSchedule().stream()
					.filter({ repayment -> repayment.getStatus() == (Repayment.Status.PAID) })
					.count() == 1
		and:
			def fromRepoLoanSchedule = loanRepository.findById(investmentId).get().getSchedule()
			fromRepoLoanSchedule.getSchedule().stream()
					.filter({ repayment -> repayment.getStatus() == (Repayment.Status.PAID) })
					.count() == 1
	}



}
