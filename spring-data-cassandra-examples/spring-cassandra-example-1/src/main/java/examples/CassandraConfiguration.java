package examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlOperations;
import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;

@Configuration
//@PropertySource(value = { "classpath:cassandra.properties" })
//@EnableCassandraRepositories(basePackages = { "example" })
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(environment.getProperty("cassandra.contactpoints"));
        cluster.setPort(Integer.parseInt(environment.getProperty("cassandra.port")));
        return cluster;
    }

    @Override
    protected String getKeyspaceName() {
        return environment.getProperty("cassandra.keyspace");
    }

    @Bean
    public CassandraMappingContext cassandraMapping() {
        return new BasicCassandraMappingContext();
    }
    @Bean
    public CassandraConverter converter() {
      return new MappingCassandraConverter(cassandraMapping());
    }
    
    @Bean(name="cqlTemplate")
    public CqlOperations cqlTemplate() throws Exception {
        return new CqlTemplate(session().getObject());
    }
    
    @Bean(name="cassandraTemplate")
	public CassandraOperations cassandraOperation() throws Exception {

		return new CassandraTemplate(session().getObject(), new MappingCassandraConverter(cassandraMapping()));
	}
}