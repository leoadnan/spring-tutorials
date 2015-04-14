package dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.ConsistencyLevel;
import org.springframework.cassandra.core.RetryPolicy;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Component;

import repository.ProductRepository;
import entity.Product;

@Component(value="productDao")
public class ProductDaoImpl implements ProductDao{

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CassandraOperations cassandraTemplate;
	
//	@Autowired
//	private ProductPagingAndSortingRepository productPagingAndSortingRepository;
	
	public void save(Product entity){
		productRepository.save(entity);
	}

	public void save(List<Product> entities){
		WriteOptions wo = new WriteOptions();
		wo.setConsistencyLevel(ConsistencyLevel.ONE);
		wo.setRetryPolicy(RetryPolicy.DOWNGRADING_CONSISTENCY);
		for (int i=0; i<entities.size();i++){
			cassandraTemplate.insertAsynchronously(entities.get(i),wo);
		}
	}
	
	public Product find(Long id){
		return productRepository.findById(id);
	}
	
//	public Page<Product> findByPage(int pageNo, int size){
//		Sort sort = new Sort(Direction.ASC,"upcId");
//		return productPagingAndSortingRepository.findAll(new PageRequest(1, 20,sort));
//		return null;
//	}
}