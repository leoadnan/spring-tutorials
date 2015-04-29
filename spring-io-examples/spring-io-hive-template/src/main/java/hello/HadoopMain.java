package hello;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HadoopMain {

	public static void main(String[] args) {
		try {
			ApplicationContext ctx = new ClassPathXmlApplicationContext("application-context.xml");
			if (ctx != null) {
				BookService service = ctx.getBean(BookService.class);
				service.showTables();
				Long count = service.count();
				System.out.println("result  : " + count);
			}
		} catch (Exception ex) {
			System.out.println("HadoopMain encountered an error and ended.");
		}
	}
}