package me.viniciuspiedade.springboot1.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.viniciuspiedade.springboot1.model.Product;
import me.viniciuspiedade.springboot1.repositories.ProductRepository;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;
	
	@Autowired
	public ProductServiceImpl(ProductRepository repository){
		this.productRepository = repository;
	}
	
	@Override
	public List<Product> getAll() {
		return productRepository.findAll();
	}

	@Override
	public Product getById(long id) {
		return productRepository.findOne(id);
	}

	@Transactional
	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	@Override
	public void remove(Product product) {
		productRepository.delete(product);
	}

	@Override
	public List<Product> getAllWithChilds() {
		return productRepository.getAllWithChilds();
	}

	@Override
	public List<Product> getChildsById(Long id) {
		return productRepository.getChildsById(id);
	}
	
	
}
