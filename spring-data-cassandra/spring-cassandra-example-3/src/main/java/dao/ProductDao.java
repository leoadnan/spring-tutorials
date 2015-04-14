package dao;

import java.util.List;

import org.springframework.stereotype.Component;

import entity.Product;

@Component
public interface ProductDao{

	public void save(Product entity);

	public void save(List<Product> entities) ;
	
	public Product find(Long id);
	
}