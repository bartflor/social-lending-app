package pl.fintech.solidlending.solidlendigplatform.appconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PostgresDatabaseConnectionConfig {

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public HikariDataSource postgresDataSource(){
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	
}
