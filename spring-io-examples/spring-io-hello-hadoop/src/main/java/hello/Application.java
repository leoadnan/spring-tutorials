package hello;

import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.hadoop.fs.FsShell;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    FsShell shell;

//    @Autowired
//    FsShell fsShell;
    
    @Override
    public void run(String... strings) throws Exception {
        System.out.println("*** HDFS content:");
        for (FileStatus fs : shell.ls("/")) {
            System.out.println(fs.getOwner() + " " +  fs.getGroup() + ": /" + fs.getPath().getName());
        }
    }

//    @Bean
//    FsShell fsShell() {
//        org.apache.hadoop.conf.Configuration hadoopConfiguration = new org.apache.hadoop.conf.Configuration();
//        hadoopConfiguration.set("fs.defaultFS", "hdfs://master:9000");
//        return new FsShell(hadoopConfiguration);
//    }
}
