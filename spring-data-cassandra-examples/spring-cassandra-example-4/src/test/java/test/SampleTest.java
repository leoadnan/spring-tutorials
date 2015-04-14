package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cassandra.core.CqlOperations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.cassandra.core.CassandraOperations;

import repository.SampleRepository;
import config.AppConfig;
import entity.SampleEntity;

public class SampleTest {

	static Logger logger = LoggerFactory.getLogger(SampleTest.class);

	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();

		CassandraOperations cassandraTemplate = (CassandraOperations) ctx.getBean("cassandraTemplate");
		
		String cql = "select count(*) from sample";
		Long count = cassandraTemplate.queryForObject(cql, Long.class);
		
		logger.info("Row Count is {}", count);

		SampleRepository sampleRepository = (SampleRepository) ctx.getBean("sampleRepository");

//		
//		for (long i = 0; i < 10000; i++) {
//			sampleRepository.save(new SampleEntity(i, "a" + i, "b" + i));
//		}
//		sampleRepository.save(new SampleEntity(8888888888888L, "testName", "b"));

		count = sampleRepository.count();
		logger.info("count is {}", count);

		count = sampleRepository.testMethod();
		logger.info("testMethod returned {}", count);

		logger.info(sampleRepository.findByName("testName").toString());

		count = sampleRepository.countByName("a1535");
		logger.info("count by name {}", count);

//		Single records inserts
		cassandraTemplate.insert(new SampleEntity(1111111111L, "Adnan", "Ahmed"));


		ctx.close();

		System.exit(0);
	}
}