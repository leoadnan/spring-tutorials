package conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@ComponentScan(basePackages = { "dao"})
@EnableCassandraRepositories(basePackages = { "repository" })
public class AuditCoreContextConfig extends AbstractCassandraConfiguration {

	@Override
	protected String getKeyspaceName() {
		return "event_owner"; 
	}

	@Override
	protected String getContactPoints() {
		return "localhost";
	}

	@Override
	protected int getPort() {
		return 9042; 
	}
}
