package pl.fintech.solidlending.solidlendigplatform.domain.loan

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
		def params = GroovyMock(LoanParams)
		def loan = LoanDomainFactory.crateLoan(randId)
		def investment = LoanDomainFactory.createInvestment()
		loan.setInvestments(Set.of(investment))
		loanFactory.createLoan(params) >> loan
		loanRepository.save(loan) >> randId
		when:
			def res = loanDomainSvc.createLoan(params)
		then:
			1*investmentRepository.save(investment)
			1*scheduleRepository.save(_)
			res == randId
	}
}
