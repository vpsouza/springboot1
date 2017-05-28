package me.viniciuspiedade.springboot1.services;

import java.util.List;

import me.viniciuspiedade.springboot1.model.Product;

public interface ProductService {

	List<Product> getAll();
	List<Product> getAllWithChilds();
	Product getById(long id);
	List<Product> getChildsById(Long id);
	Product save(Product product);
	void remove(Product product);
}
