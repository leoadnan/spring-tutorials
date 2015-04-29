package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class BookRepository {

   @Autowired
   private HiveTemplate hiveTemplate;

   public void showTables() {
      List<String> tables = hiveTemplate.query("show tables");
      System.out.println("tables size: " + tables.size());
   }

   public Long count() {
      return hiveTemplate.queryForLong("select count(*) from books;");
   }
}