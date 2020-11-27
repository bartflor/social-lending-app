package pl.fintech.solidlending.solidlendigplatform.domain.loan

import pl.fintech.solidlending.solidlendigplatform.domain.common.UserService
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanCreationException
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanNotFoundException
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.ScheduleNotFoundException
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class LoanDomainServiceImplTest extends Specification {
	def loanRepository = Mock(LoanRepository)
	def scheduleRepository = Mock(RepaymentScheduleRepository)
	def loanFactory = Mock(LoanFactory)
	def investmentFactory = Mock(InvestmentFactory)
	def investmentRepository = Mock(InvestmentRepository)
	def userService = Mock(UserService)
	@Subject
	def loanDomainSvc = new LoanDomainServiceImpl(loanRepository,
			scheduleRepository, loanFactory, investmentFactory, investmentRepository, userService)

	def "CreateLoan should save loan, loan investments and schedules to repositories"() {
		given:
			def randId = Gen.long.first()
			List<NewInvestmentParams> investmentParams = Mock()
			def params = NewLoanParams.builder()
					.investmentsParams(investmentParams)
					.build()
			def loan = LoanDomainFactory.crateLoan(randId)
			def investment = LoanDomainFactory.createInvestment()
			loan.setInvestments(Set.of(investment))

		when:
			def result = loanDomainSvc.createLoan(params)
		then:
			1*investmentFactory.createInvestmentsFrom(investmentParams) >> Set.of(investment)
			1*loanFactory.createLoan(params, Set.of(investment)) >> loan
			1*loanRepository.save(loan) >> randId
			result == randId
	}

	def "activateLoan should run loanRepository and investmentRepository activate methods when loan with given id\
		has proper status"(){
		given:
			def randId = Gen.long.first()
			def loan = LoanDomainFactory.crateLoan(randId)
			loan.setStatus(Loan.LoanStatus.UNCONFIRMED)
		when:
			loanDomainSvc.activateLoan(randId)
		then:
			1*loanRepository.findById(randId) >> Optional.of(loan)
		and:
			1*loanRepository.setActive(randId)
			1*investmentRepository.setActiveWithLoanId(randId)
	}

	def "activateLoan should throw exception when loan with given id not exists"(){
		given:
			def randId = Gen.long.first()
		when:
			loanDomainSvc.activateLoan(randId)
		then:
			1*loanRepository.findById(randId) >> Optional.empty()
		and:
			def exception = thrown(LoanNotFoundException)
			exception.getMessage() == "Loan with id:"+randId+" not found."
	}

	def "activateLoan should throw exception when loan with given id has not UNCONFIRMED status"(){
		given:
			def randId = Gen.long.first()
			def loan = LoanDomainFactory.crateLoan(randId)
			loan.setStatus(Loan.LoanStatus.ACTIVE)
		when:
			loanDomainSvc.activateLoan(randId)
		then:
			1*loanRepository.findById(randId) >> Optional.of(loan)
		and:
			def exception = thrown(LoanCreationException)
			exception.getMessage() == "Can not activate loan with id: "+randId+", loan status must be UNCONFIRMED"
	}

	def "findLoanById should throw exception when loan with given id not exist"(){
		given:
			def randId = Gen.long.first()
			loanRepository.findById(randId) >> Optional.empty()
		when:
			loanDomainSvc.findLoanById(randId)
		then:
			def exception = thrown(LoanNotFoundException)
			exception.getMessage() == "Loan with id:"+randId+" not found."
	}

}
