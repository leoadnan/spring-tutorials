package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookService {

   @Autowired 
   BookRepository bookRepo;
   
   public Long count() {
      return bookRepo.count();
   }

   public void showTables() {
      bookRepo.showTables();
   }
 }