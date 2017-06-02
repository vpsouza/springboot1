package me.viniciuspiedade.springboot1.controllers;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import me.viniciuspiedade.springboot1.exceptions.ProductException;
import me.viniciuspiedade.springboot1.model.Image;
import me.viniciuspiedade.springboot1.model.Product;
import me.viniciuspiedade.springboot1.model.Response;
import me.viniciuspiedade.springboot1.services.ProductService;
import me.viniciuspiedade.springboot1.util.ProductValidation;
import me.viniciuspiedade.springboot1.util.ValidationResult;
import me.viniciuspiedade.springboot1.util.View;

@RestController
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@JsonView(View.Summary.class)
	@RequestMapping(path = "/products", method = RequestMethod.GET)
	public ResponseEntity<List<Product>> getAllSumarized() {
		logger.info("returning all summarized products...");

		return new ResponseEntity<List<Product>>(productService.getAll(), HttpStatus.OK);
	}

	@RequestMapping(path = "/products/all", method = RequestMethod.GET)
	public ResponseEntity<List<Product>> getAll() {
		logger.info("returning all products...");

		return new ResponseEntity<List<Product>>(productService.getAllWithChilds(), HttpStatus.OK);
	}

	@JsonView(View.Summary.class)
	@RequestMapping(path = "/products/{id}", method = RequestMethod.GET)
	public ResponseEntity<Product> getByIdSumarized(@PathVariable("id") long id) throws ProductException {
		logger.info("returning a summarized product by id...");

		Product product = productService.getById(id);

		ValidationResult validationResult = ProductValidation.RestValidation.isProductExists().apply(product);
		if (!validationResult.isValid()) {
			throw new ProductException(validationResult.getViolations().get());
		}

		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}

	@RequestMapping(path = "/products/{id}/all", method = RequestMethod.GET)
	public ResponseEntity<Product> getById(@PathVariable("id") long id) throws ProductException {
		logger.info("returning a product by id...");

		Product product = productService.getById(id);

		ValidationResult validationResult = ProductValidation.RestValidation.isProductExists().apply(product);
		if (!validationResult.isValid()) {
			throw new ProductException(validationResult.getViolations().get());
		}

		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/products/{id}/images", method = RequestMethod.GET)
	public ResponseEntity<Set<Image>> getImagesByIdProduct(@PathVariable("id") long id) throws ProductException {
		logger.info("returning a product by id...");

		Product product = productService.getById(id);

		ValidationResult validationResult = ProductValidation.RestValidation.isProductExists().apply(product);
		if (!validationResult.isValid()) {
			throw new ProductException(validationResult.getViolations().get());
		}

		return new ResponseEntity<Set<Image>>(product.getImages(), HttpStatus.OK);
	}

	@RequestMapping(path = "/products/{id}/children", method = RequestMethod.GET)
	public ResponseEntity<List<Product>> getChildsById(@PathVariable("id") long id) throws ProductException {
		logger.info("returning a product by id...");

		Product product = productService.getById(id);

		ValidationResult validationResult = ProductValidation.RestValidation.isProductExists().apply(product);
		if (!validationResult.isValid()) {
			throw new ProductException(validationResult.getViolations().get());
		}
		
		return new ResponseEntity<List<Product>>(productService.getChildsById(id), HttpStatus.OK);
	}

	@RequestMapping(path = "/products/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Response> delete(@PathVariable("id") long id) throws ProductException {
		logger.info("deleting a product by id...");

		Product product = productService.getById(id);

		ValidationResult validationResult = ProductValidation.RestValidation.isProductExists().apply(product);
		if (!validationResult.isValid()) {
			throw new ProductException(validationResult.getViolations().get());
		}

		productService.remove(product);

		return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(), "Product has been deleted!"),
				HttpStatus.OK);
	}

	@RequestMapping(path = "/products", method = RequestMethod.POST)
	public ResponseEntity<Product> save(@RequestBody Product payload) throws ProductException {
		logger.info("product to save " + payload);

		ValidationResult validationResult = ProductValidation.RestValidation.isCreatePayloadValid().apply(payload);
		if (!validationResult.isValid()) {
			throw new ProductException(validationResult.getViolations().get());
		}
		
		fetchChildrenWithParend(payload);

		return new ResponseEntity<Product>(productService.save(payload), HttpStatus.OK);
	}
	
	private void fetchChildrenWithParend(Product payload){
		payload.getChildren().stream().forEach(child -> child.setParent(payload));
		payload.getImages().stream().forEach(child -> child.setProduct(payload));
		if(payload.getParent() != null && payload.getParent().getId() > 0){
			fetchChildrenWithParend(payload.getParent());
		}
	}

	@RequestMapping(path = "/products", method = RequestMethod.PATCH)
	public ResponseEntity<Product> update(@RequestBody Product payload) throws ProductException {
		logger.info("product to update " + payload);

		ValidationResult validationResult = ProductValidation.RestValidation.isUpdatePayloadValid().apply(payload);
		if (!validationResult.isValid()) {
			throw new ProductException(validationResult.getViolations().get());
		}

		Product product = productService.getById(payload.getId());
		validationResult = ProductValidation.RestValidation.isProductExists().apply(product);
		if (!validationResult.isValid()) {
			throw new ProductException(validationResult.getViolations().get());
		}
		
		fetchChildrenWithParend(payload);

		return new ResponseEntity<Product>(productService.save(payload), HttpStatus.OK);
	}

}
