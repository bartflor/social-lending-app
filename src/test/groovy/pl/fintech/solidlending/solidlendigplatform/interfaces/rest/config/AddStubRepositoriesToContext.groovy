package pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository
import pl.fintech.solidlending.solidlendigplatform.domain.auction.OfferRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.BorrowerRepository
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.LenderRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.InvestmentRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentScheduleRepository
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction.InMemoryAuctionRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction.InMemoryOfferRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction.OfferEntity
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan.InMemoryInvestmentRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan.InMemoryLoanRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan.InMemoryScheduleRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user.InMemoryUserRepo
import spock.mock.DetachedMockFactory

/**
 * this configuration will be used when in memory repositories will be moved to test scope only
 */
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
