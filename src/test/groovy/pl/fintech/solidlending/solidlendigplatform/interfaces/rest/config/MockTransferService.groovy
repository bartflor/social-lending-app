package pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import pl.fintech.solidlending.solidlendigplatform.domain.payment.PaymentService
import spock.mock.DetachedMockFactory

@TestConfiguration
class MockTransferService {

	def mockFactory = new DetachedMockFactory()

	@Bean
	@Primary
	PaymentService mockedTransferService(){
		mockFactory.Mock(PaymentService)
	}
}
