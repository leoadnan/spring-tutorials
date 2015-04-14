package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.domain.Page;

import service.ProductService;
import entity.Product;


@SpringBootApplication
@ImportResource("META-INF/spring/application-context.xml")
@ComponentScan(basePackages = {"service","dao"})
public class Application implements CommandLineRunner{
	
	@Autowired
	private ProductService productService;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
		System.exit(0);
	}
	
	public void run(String... strings){
		productService.save(new Product(1L,1L,"j4uCat","upcDsc"));
		
//		List<Product> products=new ArrayList<Product>();
//		for(int i=1; i<=10; i++){
//			products.add(new Product((long)i,(long)i,"j4uCat"+i,"upcDsc"+i));
//		}
//		System.out.println("Inserting Started: "+new Date());
//		productService.save(products);
//		System.out.println("Inserting Ended: "+new Date());
		
//		Page<Product> productsPage = productService.findByPage(1, 20);
//		for (Product product : productsPage) {
//			System.out.println(product);
//		}
		
		System.out.println(productService.find(1L));
	}
}
