package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.ProductDao;
import entity.Product;

@Component(value="productService")
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductDao productDao;
	
	public void save(Product entity){
		productDao.save(entity);
	}

	public void save(List<Product> entities){
		productDao.save(entities);
	}
	
	public Product find(Long id){
		return productDao.find(id);
	}
	
}