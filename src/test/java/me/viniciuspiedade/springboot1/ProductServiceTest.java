package me.viniciuspiedade.springboot1;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.viniciuspiedade.springboot1.model.Product;
import me.viniciuspiedade.springboot1.repositories.ProductRepository;
import me.viniciuspiedade.springboot1.services.ProductServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private ProductServiceImpl productService;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@After
	public void reset(){
		Mockito.reset(this);
	}
	
	@Test
	public void testGetAllProduct(){
		List<Product> ProductList = new ArrayList<Product>();
		ProductList.add(new Product(1l, "Product 1", "Product Sample 1", null));
		ProductList.add(new Product(2l, "Product 2", "Product Sample 2", null));
		ProductList.add(new Product(3l, "Product 3", "Product Sample 3", null));
		ProductList.add(new Product(4l, "Product 4", "Product Sample 4", null));;
		when(productRepository.findAll()).thenReturn(ProductList);
		
		List<Product> result = productService.getAll();
		assertEquals(4, result.size());
	}
	
	@Test
	public void testGetAllProductWithChilds(){
		List<Product> ProductList = new ArrayList<Product>();
		ProductList.add(new Product(1l, "Product 1", "Product Sample 1", null));
		ProductList.add(new Product(2l, "Product 2", "Product Sample 2", null));
		ProductList.add(new Product(3l, "Product 3", "Product Sample 3", null));
		ProductList.add(new Product(4l, "Product 4", "Product Sample 4", null));;
		when(productRepository.getAllWithChilds()).thenReturn(ProductList);
		
		List<Product> result = productService.getAllWithChilds();
		assertEquals(4, result.size());
	}
	
	@Test
	public void testGetChildsById(){
		List<Product> ProductList = new ArrayList<Product>();
		ProductList.add(new Product(1l, "Product 1", "Product Sample 1", null));
		ProductList.add(new Product(2l, "Product 2", "Product Sample 2", null));
		ProductList.add(new Product(3l, "Product 3", "Product Sample 3", null));
		ProductList.add(new Product(4l, "Product 4", "Product Sample 4", null));;
		when(productRepository.getChildsById(1L)).thenReturn(ProductList);
		
		List<Product> result = productService.getChildsById(1L);
		assertEquals(4, result.size());
	}
	
	@Test
	public void testGetProductById(){
		Product Product = new Product(1l, "Product 1", "Product Sample 1", null);
		when(productRepository.findOne(1L)).thenReturn(Product);
		Product result = productService.getById(1);
		
		assertEquals(true, new Long(1).equals(result.getId()));
		assertEquals("Product 1", result.getName());
		assertEquals("Product Sample 1", result.getDescription());
	}
	
	@Test
	public void saveProduct(){
		Product Product = new Product(8l, "Product 8", "Product Sample 8", null);
		when(productRepository.save(Product)).thenReturn(Product);
		Product result = productService.save(Product);
		assertEquals(true, new Long(8).equals(result.getId()));
		assertEquals("Product 8", result.getName());
		assertEquals("Product Sample 8", result.getDescription());
	}
	
	@Test
	public void removeProduct(){
		Product Product = new Product(8l, "Product 8", "Product Sample 8", null);
		productService.remove(Product);
        verify(productRepository, times(1)).delete(Product);
	}
}
