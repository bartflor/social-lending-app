package pl.fintech.solidlending.solidlendigplatform.interfaces.rest.config

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Specification

class PostgresContainerTestSpecification extends Specification {
	static def USER = 'container_usr'
	static PostgreSQLContainer postgreSQLContainer

	static {
		postgreSQLContainer = new PostgreSQLContainer<>("postgres:9.6")
				.withUsername(USER)
				.withPassword(USER)
		postgreSQLContainer.start()
	}

	@DynamicPropertySource
	static void datasourceConfig(DynamicPropertyRegistry registry) {
		registry.add('spring.datasource.jdbc-url', { -> postgreSQLContainer.getJdbcUrl() })
		registry.add('spring.datasource.username', { -> USER })
		registry.add('spring.datasource.password', { -> USER })

	}

}