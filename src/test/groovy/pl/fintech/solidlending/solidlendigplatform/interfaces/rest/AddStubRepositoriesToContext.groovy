package pl.fintech.solidlending.solidlendigplatform.interfaces.rest

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionRepository
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanRepository
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction.InMemoryAuctionRepo
import pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan.InMemoryLoanRepo
import spock.mock.DetachedMockFactory

/**
 * this configuration will be used when in memory repositories will be moved to test scope only
 */
@TestConfiguration
class AddStubRepositoriesToContext {


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

}
