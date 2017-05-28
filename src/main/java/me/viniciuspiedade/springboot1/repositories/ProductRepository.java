package me.viniciuspiedade.springboot1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import me.viniciuspiedade.springboot1.model.Product;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT distinct p FROM Product p where p.parent is null")
	List<Product> findAll();
	
	@Query("SELECT distinct p FROM Product p LEFT JOIN FETCH p.children LEFT JOIN FETCH p.images where (p.parent is null)")
	List<Product> getAllWithChilds();
	
	@Query("SELECT distinct p FROM Product p LEFT JOIN FETCH p.children LEFT JOIN FETCH p.images where p.parent.id = ?1")
	List<Product> getChildsById(Long id);
	
	@Query("SELECT distinct p FROM Product p LEFT JOIN FETCH p.children LEFT JOIN FETCH p.images where p.id = ?1")
	Product findOne(Long id);
	
}
