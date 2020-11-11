package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ApplicationListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ContextClosedEvent

class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static int RANDOM_PORT = 0

    static WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(RANDOM_PORT))

    @Override
    void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.beanFactory.registerSingleton('wireMockServer', wireMockServer)
        wireMockServer.start()
        TestPropertyValues.of("bank.api.url=http://localhost:${wireMockServer.port()}").applyTo(applicationContext)
        TestPropertyValues.of("bank.api.user=usr").applyTo(applicationContext)
        TestPropertyValues.of("bank.api.password=pass").applyTo(applicationContext)
        applicationContext.addApplicationListener(new WireMockShutdownListener(wireMockServer))
    }

    static class WireMockShutdownListener implements ApplicationListener<ContextClosedEvent> {
        final WireMockServer server

        WireMockShutdownListener(WireMockServer server) {
            this.server = server
        }

        @Override
        void onApplicationEvent(ContextClosedEvent event) {
            server.stop()
        }
    }
}
