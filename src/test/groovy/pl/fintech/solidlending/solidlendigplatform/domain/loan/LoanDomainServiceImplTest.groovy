package pl.fintech.solidlending.solidlendigplatform.domain.loan

import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanCreationException
import pl.fintech.solidlending.solidlendigplatform.domain.loan.exception.LoanNotFoundException
import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Subject

class LoanDomainServiceImplTest extends Specification {
	def loanRepository = Mock(LoanRepository)
	def scheduleRepository = Mock(RepaymentScheduleRepository)
	def loanFactory = Mock(LoanFactory)
	def investmentRepository = Mock(InvestmentRepository)

	@Subject
	def loanDomainSvc = new LoanDomainServiceImpl(loanRepository, scheduleRepository, loanFactory, investmentRepository)

	def "CreateLoan should save loan, loan investments and schedule to repositories"() {
		given:
		def randId = Gen.long.first()
		def params = GroovyMock(NewLoanParams)
		def loan = LoanDomainFactory.crateLoan(randId)
		def investment = LoanDomainFactory.createInvestment()
		loan.setInvestments(Set.of(investment))
		loanFactory.createLoan(params) >> loan
		loanRepository.save(loan) >> randId
		when:
			def res = loanDomainSvc.createLoan(params)
		then:
			1*investmentRepository.save(investment)
			2*scheduleRepository.save(_)
			res == randId
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
			thrown(LoanNotFoundException)
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
			thrown(LoanCreationException)
	}

	def "findLoanById should throw exception when loan with given id not exist"(){
		given:
			def randId = Gen.long.first()
			loanRepository.findById(randId) >> Optional.empty()
		when:
			loanDomainSvc.findLoanById(randId)
		then:
			thrown(LoanNotFoundException)
	}
}
