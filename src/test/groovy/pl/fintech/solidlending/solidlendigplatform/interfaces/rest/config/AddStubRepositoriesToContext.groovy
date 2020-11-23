package pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config


import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository
import pl.fintech.solidlending.solidlendigplatform.domain.auction.OfferRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.InvestmentRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentScheduleRepository
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory.InMemoryAuctionRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory.InMemoryOfferRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory.InMemoryInvestmentRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory.InMemoryLoanRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory.InMemoryScheduleRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.InMemory.InMemoryUserRepo

@TestConfiguration
class AddStubRepositoriesToContext {

	@Primary
	@Bean
	BorrowerRepository borrowerRepository(){
		return new InMemoryUserRepo()
	}

	@Primary
	@Bean
	InvestmentRepository investmentRepository(){
		return new InMemoryInvestmentRepo()
	}

	@Primary
	@Bean
	OfferRepository offerRepository(){
		return new InMemoryOfferRepo()
	}

	@Primary
	@Bean
	AuctionRepository auctionRepository(){
		return new InMemoryAuctionRepo()
	}

	@Primary
	@Bean
	LoanRepository loanRepository(){
		return new InMemoryLoanRepo()
	}

	@Primary
	@Bean
	RepaymentScheduleRepository scheduleRepository(){
		return new InMemoryScheduleRepo()
	}

}
