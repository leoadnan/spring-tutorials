package hello;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.data.hadoop.store.codec.Codecs;
import org.springframework.data.hadoop.store.output.DelimitedTextFileWriter;
import org.springframework.data.hadoop.store.output.TextFileWriter;
import org.springframework.data.hadoop.store.strategy.naming.StaticFileNamingStrategy;

@SpringBootApplication
@ImportResource("application-context.xml")
public class Application implements CommandLineRunner{

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	
	public static void main(String args[]) throws IOException{
		ApplicationContext ctx= SpringApplication.run(Application.class, args);

	}
	
	@Autowired
	Configuration hadoopConfiguration;
	
	@Autowired
	FsShell hdfsShell;
	
	
	@Override
	public void run(String... args) throws IOException{
		LOGGER.info("Context loaded successfully");
		Path path = new Path(".");
		TextFileWriter writer = new TextFileWriter(hadoopConfiguration, path,null);
		StaticFileNamingStrategy fileNameStrategy=new StaticFileNamingStrategy("store_1.csv" );
		writer.setFileNamingStrategy(fileNameStrategy);
		writer.write("ABC");
		writer.write("adfasd");
		writer.close();
//		List<String> entities = new ArrayList();
//		entities.add("1");
//		entities.add("2");
//		entities.add("3");
		DelimitedTextFileWriter dtw=new DelimitedTextFileWriter(hadoopConfiguration, path, null);
//		dtw.write(entities);
	}
}


