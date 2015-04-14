package examples;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cassandra.core.CqlOperations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.utils.UUIDs;
import com.google.common.collect.ImmutableSet;

@SpringBootApplication
@PropertySource(value = { "classpath:cassandra.properties" })
@EnableCassandraRepositories(basePackages = { "examples" })
public class Application {
	
	
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		
//		CassandraConfiguration cassandraConf=ctx.getBean(CassandraConfiguration.class);
		
		System.out.println("Using CqlTemplate: ");
//		CqlOperations cqlTemplate = cassandraConf.cqlTemplate();
		CqlOperations cqlTemplate = (CqlOperations)ctx.getBean("cqlTemplate");

		cqlTemplate.execute("insert into event (id, type, bucket, tags) values (" + UUIDs.timeBased() + ", 'type1', '2014-01-01', {'tag2', 'tag3'})");
		
		Insert insert1 = QueryBuilder.insertInto("event").value("id", UUIDs.timeBased())
				.value("type", "type2").value("bucket", "2014-01-01").value("tags", ImmutableSet.of("tag1"));
		cqlTemplate.execute(insert1);
		
		Statement insert2 = cqlTemplate.getSession().
				prepare("insert into event (id, type, bucket, tags) values (?, ?, ?, ?)").
				bind(UUIDs.timeBased(), "type2", "2014-01-01", ImmutableSet.of("tag1", "tag2"));
		cqlTemplate.execute(insert2);

		ResultSet rs1 = cqlTemplate.query("select * from event where type='type2' and bucket='2014-01-01'");
		
		System.out.println("ResultSet-1: ");
		for (Row row : rs1) {
			System.out.format("%s, %s, %s, %s\n",row.getUUID("id").toString(),row.getString("type"),row.getString("bucket"), row.getSet("tags", String.class).toString());
		}
		
		
		Select select = QueryBuilder.select().from("event").
				where(QueryBuilder.eq("type", "type1")).and(QueryBuilder.eq("bucket", "2014-01-01")).limit(10);
		ResultSet rs2 = cqlTemplate.query(select);
		System.out.println("ResultSet-2: ");
		for (Row row : rs2) {
			System.out.format("%s, %s, %s, %s\n",row.getUUID("id").toString(),row.getString("type"),row.getString("bucket"), row.getSet("tags", String.class).toString());
		}
		
		System.out.println("Using CassandraTemplate: ");
//		CassandraOperations cassandraTemplate = cassandraConf.cassandraTemplate();
		CassandraOperations cassandraTemplate = (CassandraOperations)ctx.getBean("cassandraTemplate");
		
		cassandraTemplate.insert(new Event(UUIDs.timeBased(), "type3", "2014-01-01", ImmutableSet.of("tag1", "tag3")));

//		Event oneEvent = cassandraTemplate.selectOne(select, Event.class);
//		System.out.println(oneEvent);
		List<Event> moreEvents = cassandraTemplate.select(select, Event.class);
		for (Event event : moreEvents) {
			System.out.println(event);
		}
		
		
		System.out.println("Using spring repository for cassandra data store");
		EventRepository eventRepo = ctx.getBean(EventRepository.class);
		eventRepo.save(new Event(UUIDs.timeBased(), "type1", "2014-01-01", ImmutableSet.of("tag1", "tag2")));
//
		Iterable<Event> events = eventRepo.findByTypeAndBucket("type1", "2014-01-01");
		for (Event event : events) {
			System.out.println(event);
		}
		System.exit(0);
	}
}
