package repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import entity.Product;

public interface ProductRepository extends CassandraRepository<Product>{

    @Query("select * from product where upc_id = ?0")
    Product findById(Long id);

}
