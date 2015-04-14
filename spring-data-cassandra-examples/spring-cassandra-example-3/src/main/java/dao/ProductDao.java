package dao;

import java.util.List;

import entity.Product;

public interface ProductDao{

	public void save(Product entity);

	public void save(List<Product> entities) ;
	
	public Product find(Long id);
	
//	public Page<Product> findByPage(int pageNo, int size);
}