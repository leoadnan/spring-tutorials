package config;


import javax.annotation.Resource;

import org.springframework.cassandra.config.java.AbstractSessionConfiguration;
import org.springframework.cassandra.core.CqlOperations;
import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
//@ComponentScan({"com.cooldatasoft"})
@EnableCassandraRepositories(basePackages = {"repository"})
public class AppConfig extends AbstractSessionConfiguration {

    @Resource
    private Environment env;

    @Override
    protected String getKeyspaceName() {
        return "test";
    }

    @Bean
    public CqlOperations cqlTemplate() throws Exception {
        return new CqlTemplate(session().getObject());
    }


    @Bean
    public CassandraOperations cassandraTemplate() throws Exception {
        return new CassandraTemplate(session().getObject());
    }

}