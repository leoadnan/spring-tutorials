package test;

 
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.RetryPolicy;
import org.springframework.cassandra.core.RowMapper;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Truncate;
import com.datastax.driver.core.querybuilder.Update;

import repository.ProductRepository;
import service.ProductService;
import dao.ProductDao;
import entity.Product;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/application-context.xml" })
public class ProductTest {
 
	static Logger logger = LoggerFactory.getLogger(ProductTest.class);

    @Autowired
    public ProductService productService;
 
    @Autowired
    public ProductDao productDao;
    
    @Autowired
    public CassandraOperations cassandraTemplate;
    
    @Autowired
    public ProductRepository productRepository;
    
    @Before
    public void setUp() throws Exception {
    	
    }
    
    @Test
    public void test() {
    	insert();
    	select();
    	update();
    	delete();
    	truncate();
    }

    public void insert(){
    	
    	logger.info("******************Insert case started*******************");
    	
    	//Insert a record with an annotated POJO.
    	cassandraTemplate.insert(new Product(1L, 1, "j4u_cat_1", "upc_dsc_1"));
    	
    	logger.info("Inserted record by annotate pojo {}",productDao.find(1L));
    	
    	//Insert a row using the QueryBuilder.Insert object that is part of the DataStax Java Driver.
    	Insert insert = QueryBuilder.insertInto("product");
    	insert.setConsistencyLevel(ConsistencyLevel.ONE);
    	insert.value("upc_id", 2L).value("cat_id", 2).value("j4u_cat", "j4u_cat_2").value("upc_dsc", "upc_dsc_2");
    	cassandraTemplate.execute(insert);
    	
    	//Then there is always the old fashioned way. You can write your own CQL statements.
    	String cql = "insert into product (upc_id, cat_id, j4u_cat, upc_dsc) values (3, 3, 'j4u_cat_3','upc_dsc_3')";
    	cassandraTemplate.execute(cql);
    	
    	//Multiple inserts for high speed ingestion
    	String cqlIngest = "insert into product (upc_id, cat_id, j4u_cat, upc_dsc) values (?,?,?,?)";
    	
    	List<Object> product1 = new ArrayList<Object>();
    	product1.add(101L);
    	product1.add(101);
    	product1.add("j4u_cat_10001");
    	product1.add("upc_dsc_10001");
    	
    	List<Object> product2 = new ArrayList<Object>();
    	product2.add(102L);
    	product2.add(102);
    	product2.add("j4u_cat_10002");
    	product2.add("upc_dsc_10002");

    	List<List<?>> products = new ArrayList<List<?>>();
    	products.add(product1);
    	products.add(product2);

    	WriteOptions wo = new WriteOptions();
    	wo.setConsistencyLevel(org.springframework.cassandra.core.ConsistencyLevel.ONE);
    	wo.setRetryPolicy(RetryPolicy.DOWNGRADING_CONSISTENCY);
    	cassandraTemplate.ingest(cqlIngest, products,wo);
    	
    	
//    	products = new ArrayList<List<?>>();
//    	for (int i=10000; i<100000; i++){
//	    	List<Object> product = new ArrayList<Object>();
//	    	product.add(new Long(i));
//	    	product.add(i);
//	    	product.add("j4u_cat_"+i);
//	    	product.add("upc_dsc_"+i);
//	    	products.add(product);
//    	}
    	cassandraTemplate.ingest(cqlIngest, products,wo);

    	logger.info("******************Insert case ended*******************");
    }
    public void select(){
    	
    	logger.info("total products {}",productRepository.count());
    	
    	//Query a table for multiple rows and map the results to a POJO.
    	String cqlAll = "select * from product";

    	List<Product> results = cassandraTemplate.select(cqlAll, Product.class);
    	for (Product p : results) {
    		logger.info(String.format("Found product with Name [%s] for id [%s]", p.getUpcId(), p.getUpcDsc()));
    	}
    	
    	//Query a table for a single row and map the result to a POJO.
    	String cqlOne = "select * from product where upc_id = 1";

    	Product p = cassandraTemplate.selectOne(cqlOne, Product.class);
    	logger.info(String.format("Found product with Name [%s] for id [%s]", p.getUpcId(), p.getUpcDsc()));
    	
    	//Query a table using the QueryBuilder.Select object that is part of the DataStax Java Driver.
    	Select select = QueryBuilder.select().from("product");
    	select.where(QueryBuilder.eq("upc_id", 1L));

    	p = cassandraTemplate.selectOne(select, Product.class);
    	logger.info(String.format("Found product with Name [%s] for id [%s]", p.getUpcId(), p.getUpcDsc()));
    	
    	//Then there is always the old fashioned way. You can write your own CQL statements, 
    	
    	cqlAll = "select * from product";
    	results = cassandraTemplate.query(cqlAll, new RowMapper<Product>() {

    		public Product mapRow(Row row, int rowNum) throws DriverException {
    			Product p = new Product(row.getLong("upc_id"), row.getInt("cat_id"), row.getString("j4u_cat"),row.getString("upc_dsc"));
    			return p;
    		}
    	});

    	for (Product x : results) {
        	logger.info(String.format("Found product with Name [%s] for id [%s]", x.getUpcId(), x.getUpcDsc()));
    	}
    }
    public void update(){
    	logger.info("******************Update case started*******************");
    	
    	//Update a record with an annotated POJO.
    	cassandraTemplate.update(new Product(1L,  "upc_dsc_update_1"));
    	
    	logger.info("updated record by annotate pojo {}",productDao.find(1L));
    	
    	//Update a row using the QueryBuilder.Update object that is part of the DataStax Java Driver.
    	Update update = QueryBuilder.update("product");
    	update.setConsistencyLevel(ConsistencyLevel.ONE);
    	update.with(QueryBuilder.set("j4u_cat", "j4u_cat_update_2"));
    	update.with(QueryBuilder.set("upc_dsc", "upc_dsc_update_2"));
    	update.where(QueryBuilder.eq("upc_id", 2L));
    	cassandraTemplate.execute(update);

    	logger.info("updated record by querybuilder {}",productDao.find(2L));
    	
    	//Then there is always the old fashioned way. You can write your own CQL statements.
    	String cql = "update product set j4u_cat = 'j4u_cat_update_3', upc_dsc='upc_dsc_update_3' where upc_id = 3";
    	cassandraTemplate.execute(cql);
    	logger.info("updated record by cql {}",productDao.find(3L));
    	
    	logger.info("******************update case ended*******************");    	
    }
    
    public void delete(){
    	logger.info("******************delete case started*******************");
    	
    	//Update a record with an annotated POJO.
//    	cassandraTemplate.delete(new Product(1L,null));
    	
//    	logger.info("deleted record by annotate pojo {}",productDao.find(1L));
    	
//    	Delete a row using the QueryBuilder.Delete object that is part of the DataStax Java Driver.

    	Delete delete = QueryBuilder.delete().from("product");
    	delete.where(QueryBuilder.eq("upc_id", 1L));

    	cassandraTemplate.execute(delete);

    	logger.info("deleted record by querybuilder {}",productDao.find(1L));

//    	Then there is always the old fashioned way. You can write your own CQL statements.

    	String cql = "delete from product where upc_id = 2";

    	cassandraTemplate.execute(cql);
    	
    	logger.info("deleted record by cql {}",productDao.find(2L));

    	logger.info("******************delete case ended*******************");    	
    }
    
    
    public void truncate(){
    	cassandraTemplate.truncate("product");
    	
    	Truncate truncate = QueryBuilder.truncate("product");
    	cassandraTemplate.execute(truncate);

    	String cql = "truncate product";

    	cassandraTemplate.execute(cql);
    }
}