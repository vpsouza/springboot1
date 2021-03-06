package me.viniciuspiedade.springboot1;

import java.util.Arrays;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import me.viniciuspiedade.springboot1.model.Image;
import me.viniciuspiedade.springboot1.model.Product;
import me.viniciuspiedade.springboot1.repositories.ImageRepository;
import me.viniciuspiedade.springboot1.repositories.ProductRepository;

@SpringBootApplication
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
//            	registry.addMapping("/*").allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD").allowedOrigins("http://localhost:3000");
//            	registry.addMapping("/*/*").allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD").allowedOrigins("http://localhost:3000");
//            	registry.addMapping("/*/*/*").allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD").allowedOrigins("http://localhost:3000");
            	registry.addMapping("/*").allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD").allowedOrigins("https://vpsouza-springboot1-react.firebaseapp.com");
            	registry.addMapping("/*/*").allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD").allowedOrigins("https://vpsouza-springboot1-react.firebaseapp.com");
            	registry.addMapping("/*/*/*").allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD").allowedOrigins("https://vpsouza-springboot1-react.firebaseapp.com");
            }
        };
    }
	
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	    MappingJackson2HttpMessageConverter converter = 
	        new MappingJackson2HttpMessageConverter(mapper);
	    return converter;
	}
	
	@Bean
	@Transactional
	public CommandLineRunner setup(ProductRepository productRepository, ImageRepository imageRepository) {
		return (args) -> {
			Arrays.asList(1,2,3,4,5,6).stream().forEach(idx -> {
				
				final Product product = new Product("Product " + idx, "A Product " + idx, null);
				product.getImages().add(new Image("IMG1", product));
				product.getImages().add(new Image("IMG2", product));
				product.getImages().add(new Image("IMG3", product));
				final Product parent = createProduct(productRepository, product);
				
				Arrays.asList(1,2,3,4,5,6).stream().forEach(idxChild -> {
					createProduct(productRepository, new Product("Child Product " + idxChild, "A Child Product " + idxChild + " of Parent " + parent.getName(), parent));
				});
			});
			
			logger.info("The sample data has been generated");
		};
	}
	
	@Transactional
	private Product createProduct(ProductRepository productRepository, Product p){
		return productRepository.saveAndFlush(p);
	}
}
