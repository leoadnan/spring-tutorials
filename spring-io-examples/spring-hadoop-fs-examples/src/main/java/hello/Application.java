package hello;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.data.hadoop.fs.HdfsResourceLoader;

@SpringBootApplication
@ImportResource("application-context.xml")
public class Application implements CommandLineRunner{

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	
	public static void main(String args[]) throws IOException{
		ApplicationContext ctx= SpringApplication.run(Application.class, args);

	}
	
	//Getting this instance because of spring-data-hadoop-boot
	@Autowired
	FsShell hdfsShell;
	
	//Getting this instance because of spring-data-hadoop-boot
	@Autowired
	Configuration hadoopConfiguration;
	
	//This is defined in application-context.xml
	@Autowired
	FileSystem hadoopFs;
	
	//This is defined in application-context.xml
//	@Autowired
//	FileSystem webHdfsShell;
	
	@Autowired
	HdfsResourceLoader loader;
	
	@Autowired
//	@Qualifier(value="loaderWithUser")
	HdfsResourceLoader loaderWithUser;
	
	@Override
	public void run(String... args) throws IOException{
		LOGGER.info("Context loaded successfully");
		
		Resource resource =loader.getResource("my.file");
		Resource resource2 = loaderWithUser.getResource("store.csv");
		
		LOGGER.info(resource.contentLength()+":"+resource.getURI().toString());
		LOGGER.info(resource2.contentLength()+":"+resource2.getURI().toString());
		
		
		
		 for (FileStatus fs : hdfsShell.ls("/")) {
	            System.out.println(fs.getOwner() + " " +  fs.getGroup() + ": /" + fs.getPath().getName());
	        }
	}
}


