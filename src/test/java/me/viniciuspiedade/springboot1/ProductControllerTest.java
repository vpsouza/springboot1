package me.viniciuspiedade.springboot1;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import me.viniciuspiedade.springboot1.model.Image;
import me.viniciuspiedade.springboot1.model.Product;
import me.viniciuspiedade.springboot1.services.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext wac;
	
	@MockBean
	private ProductService productService;

	@Before
	public void setup() {
		//Mockito.reset(productService);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	private List<Product> getMockListAll(boolean isAll){
		return Arrays.asList(1L,2L,3L,4L,5L,6L).stream().map(idProd -> {
			Product p = new Product(idProd, "p" + idProd, "Product " + idProd, null);
			
			if(isAll)
				Arrays.asList(2L,3L,4L,5L,6L).stream().forEach(idChild -> {
					p.getChildren().add(new Product(idChild, "cp" + idChild, "Child " + idChild, p));
				});
			
			return p;
		}).collect(Collectors.toList());
	}
	
	private Product getMockById(boolean all){
		Product parent = new Product(1l, "Product 1", "A Product 1", null);
		
		if(all){
			parent.getChildren().addAll(Arrays.asList(2l, 3l, 4l).stream().map(idProd -> new Product(idProd, "Child Product " + idProd, "A Child Product " + idProd, parent)).collect(Collectors.toList()));
			parent.getImages().addAll(Arrays.asList(2l, 3l, 4l).stream().map(idImg -> new Image(idImg, "Img type " + idImg, parent)).collect(Collectors.toList()));
		}
		
		return parent;
	}
	
	@Test
	public void verifyProductList() throws Exception {
		given(productService.getAll()).willReturn(getMockListAll(false));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/products").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(6))).andDo(print());
	}
	
	@Test
	public void verifyProductAllList() throws Exception {
		when(productService.getAllWithChilds()).thenReturn(getMockListAll(true));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/products/all").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(6)))
			.andDo(print());
	}
	
	@Test
	public void verifyProductById() throws Exception {
		given(productService.getById(1l)).willReturn(new Product(1l, "Product 1", "A Product 1", null));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/products/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.name").exists())
		.andExpect(jsonPath("$.description").exists())
		.andExpect(jsonPath("$.id").value(1l))
		.andExpect(jsonPath("$.name").value("Product 1"))
		.andExpect(jsonPath("$.description").value("A Product 1"))
		.andDo(print());
	}
	
	@Test
	public void verifyInvalidProductArgument() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/products/f").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.errorCode").value(500))
			.andDo(print());
	}
	
	@Test
	public void verifyInvalidProductId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/products/0").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.errorCode").value(404))
		.andExpect(jsonPath("$.violations", hasSize(1)))
		.andExpect(jsonPath("$.violations[0].errorMessage").value("Product does not exists"))
		.andExpect(jsonPath("$.violations[0].error").value("PRODUCT_NOT_EXISTS"))
		.andDo(print());
	}
	
	@Test
	public void verifyProductByIdAll() throws Exception {
		
		given(productService.getById(1l)).willReturn(getMockById(true));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/products/1/all").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.name").exists())
		.andExpect(jsonPath("$.description").exists())
		.andExpect(jsonPath("$.id").value(1l))
		.andExpect(jsonPath("$.name").value("Product 1"))
		.andExpect(jsonPath("$.description").value("A Product 1"))
		.andExpect(jsonPath("$.children").exists())
		.andExpect(jsonPath("$.children", hasSize(3)))
		.andExpect(jsonPath("$.images").exists())
		.andExpect(jsonPath("$.images", hasSize(3)))
		.andDo(print());
	}
	
	@Test
	public void verifyProductByIdIMages() throws Exception {
		given(productService.getById(1l)).willReturn(getMockById(true));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/products/1/images").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[0].id").exists())
		.andExpect(jsonPath("$[0].type").exists())
		.andDo(print());
	}
	
	@Test
	public void verifyProductByIdChildren() throws Exception {
		given(productService.getById(1l)).willReturn(getMockById(true));
		given(productService.getChildsById(1l)).willReturn(getMockListAll(false));
		mockMvc.perform(MockMvcRequestBuilders.get("/products/1/children").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$", hasSize(6)))
		.andExpect(jsonPath("$[0].id").exists())
		.andExpect(jsonPath("$[0].name").exists())
		.andExpect(jsonPath("$[0].description").exists())
		.andDo(print());
	}
	
	@Test
	public void verifyDeleteProduct() throws Exception {
		given(productService.getById(1l)).willReturn(getMockById(true));
		Mockito.doNothing().when(productService).remove(any(Product.class));
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/products/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value(200))
		.andExpect(jsonPath("$.message").value("Product has been deleted!"))
		.andDo(print());
	}
	
	@Test
	public void verifyInvalidProductIdToDelete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/products/99").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.errorCode").value(404))
		.andExpect(jsonPath("$.violations", hasSize(1)))
		.andExpect(jsonPath("$.violations[0].errorMessage").value("Product does not exists"))
		.andExpect(jsonPath("$.violations[0].error").value("PRODUCT_NOT_EXISTS"))
		.andDo(print());
	}
	
	@Test
	public void verifySaveProduct() throws Exception {
		when(productService.save(any(Product.class))).thenReturn(getMockById(true));
		//then(productService).should(times(1)).save(any(Product.class));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/products/")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{ \"name\": \"Iphone 7 Plus\", \"description\": \"Iphone 7 Plus 256gb Black Piano\" }")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.name").exists())
		.andExpect(jsonPath("$.description").exists())
		.andDo(print());
	}
	
	@Test
	public void verifyMalformedSaveProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/products/")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{ \"id\": 43, \"name\": \"Iphone 7 Plus\", \"description\": \"Iphone 7 Plus 256gb Black Piano\" }")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errorCode").value(404))
		.andExpect(jsonPath("$.violations", hasSize(1)))
		.andExpect(jsonPath("$.violations[0].errorMessage").value("Invalid request body - id must not be defined."))
		.andExpect(jsonPath("$.violations[0].error").value("PRODUCT_CREATE_PAYLOAD_NOT_WELL_DEFINED"))
		.andDo(print());
	}
	
	@Test
	public void verifyUpdateProduct() throws Exception {
		given(productService.getById(1l)).willReturn(getMockById(true));
		when(productService.save(any(Product.class))).thenReturn(getMockById(true));
		
		mockMvc.perform(MockMvcRequestBuilders.patch("/products/")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{ \"id\": 1, \"name\": \"Iphone 7 Plus\", \"description\": \"New Description is Iphone 7 Plus 256gb Black Piano\" }")
        .accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.name").exists())
		.andExpect(jsonPath("$.description").exists())
		.andExpect(jsonPath("$.id").value(1))
		.andDo(print());
	}
	
	@Test
	public void verifyInvalidProductUpdate() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch("/products/")
		.content("{ \"id\": \"99\", \"name\": \"Iphone 7 Plus\", \"description\": \"New Description is Iphone 7 Plus 256gb Black Piano\" }")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errorCode").value(404))
		.andExpect(jsonPath("$.violations", hasSize(1)))
		.andExpect(jsonPath("$.violations[0].errorMessage").value("Product does not exists"))
		.andExpect(jsonPath("$.violations[0].error").value("PRODUCT_NOT_EXISTS"))
		.andDo(print());
	}

}
