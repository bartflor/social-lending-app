package pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import pl.fintech.solidlending.solidlendigplatform.domain.auction.AuctionApplicationService
import pl.fintech.solidlending.solidlendigplatform.domain.loan.LoanApplicationService
import spock.mock.DetachedMockFactory

@TestConfiguration
class AddMockedServiceToContext {

	def mockFactory = new DetachedMockFactory()

	@Primary
	@Bean
	AuctionApplicationService auctionApplicationService(){
		mockFactory.Mock(AuctionApplicationService)
	}

	@Primary
	@Bean
	LoanApplicationService loanApplicationService(){
		mockFactory.Mock(LoanApplicationService)
	}

}
