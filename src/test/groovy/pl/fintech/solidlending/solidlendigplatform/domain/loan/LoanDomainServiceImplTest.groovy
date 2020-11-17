package pl.fintech.solidlending.solidlendigplatform.domain.loan

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

	@Subject
	def loanDomainSvc = new LoanDomainServiceImpl(loanRepository, scheduleRepository, loanFactory, investmentFactory, investmentRepository)

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
			investmentFactory.createInvestmentsFrom(investmentParams) >> Set.of(investment)
			loanFactory.createLoan(params, Set.of(investment)) >> loan
			loanRepository.save(loan) >> randId
		when:
			def result = loanDomainSvc.createLoan(params)
		then:
			1*investmentRepository.save(investment)
			2*scheduleRepository.save(_)
			result == randId
	}

	def "activateLoan should call repositories activate method when loan with given id exists"(){
		given:
			def randId = Gen.long.first()
		when:
			loanDomainSvc.activateLoan(randId)
		then:
			1*loanRepository.findById(randId) >> Optional.of(LoanDomainFactory.crateLoan(randId))
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
			exception.getMessage() == "Can not activate loan with status: ACTIVE"
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

	def "findLoanRepaymentSchedule should throw exception when schedule with given id not exist"(){
		given:
			def randId = Gen.long.first()
			scheduleRepository.findRepaymentScheduleByLoanId(randId) >> Optional.empty()
		when:
			loanDomainSvc.findLoanRepaymentSchedule(randId)
		then:
			def exception = thrown(ScheduleNotFoundException)
			exception.getMessage() == "Repayment schedule for loan with id:"+randId+", not found"
	}

	def "findInvestmentRepaymentSchedule should throw exception when schedule with given id not exist"(){
		given:
			def randId = Gen.long.first()
			scheduleRepository.findRepaymentScheduleByInvestmentId(randId) >> Optional.empty()
		when:
			loanDomainSvc.findInvestmentRepaymentSchedule(randId)
		then:
			def exception = thrown(ScheduleNotFoundException)
			exception.getMessage() == "Repayment schedule for loan with id:"+randId+", not found"
	}
}
