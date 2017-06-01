package me.viniciuspiedade.springboot1;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode=ClassMode.BEFORE_CLASS)
public class ProductControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext wac;

	@Before
	public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void verifyProductList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/products").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(5))).andDo(print());
	}
	
	@Test
	public void verifyProductAllList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/products/all").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(5)))
			.andDo(print());
	}
	
	@Test
	public void verifyProductById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/products/8").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.name").exists())
		.andExpect(jsonPath("$.description").exists())
		.andExpect(jsonPath("$.id").value(8))
		.andExpect(jsonPath("$.name").value("Product 2"))
		.andExpect(jsonPath("$.description").value("A Product 2"))
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
		mockMvc.perform(MockMvcRequestBuilders.get("/products/8/all").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.name").exists())
		.andExpect(jsonPath("$.description").exists())
		.andExpect(jsonPath("$.id").value(8))
		.andExpect(jsonPath("$.name").value("Product 2"))
		.andExpect(jsonPath("$.description").value("A Product 2"))
		.andExpect(jsonPath("$.children").exists())
		.andExpect(jsonPath("$.images").exists())
		.andDo(print());
	}
	
	@Test
	public void verifyProductByIdIMages() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/products/8/images").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[0].id").exists())
		.andExpect(jsonPath("$[0].type").exists())
		.andDo(print());
	}
	
	@Test
	public void verifyProductByIdChildren() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/products/8/children").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$", hasSize(5)))
		.andExpect(jsonPath("$[0].id").exists())
		.andExpect(jsonPath("$[0].name").exists())
		.andExpect(jsonPath("$[0].description").exists())
		.andDo(print());
	}
	
	@Test
	public void verifyDeleteProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/products/8").accept(MediaType.APPLICATION_JSON))
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
		mockMvc.perform(MockMvcRequestBuilders.post("/products/")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{ \"name\": \"Iphone 7 Plus\", \"description\": \"Iphone 7 Plus 256gb Black Piano\" }")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.name").exists())
		.andExpect(jsonPath("$.description").exists())
		.andExpect(jsonPath("$.name").value("Iphone 7 Plus"))
		.andExpect(jsonPath("$.description").value("Iphone 7 Plus 256gb Black Piano"))
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
		mockMvc.perform(MockMvcRequestBuilders.patch("/products/")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{ \"id\": 8, \"name\": \"Iphone 7 Plus\", \"description\": \"New Description is Iphone 7 Plus 256gb Black Piano\" }")
        .accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.name").exists())
		.andExpect(jsonPath("$.description").exists())
		.andExpect(jsonPath("$.id").value(8))
		.andExpect(jsonPath("$.name").value("Iphone 7 Plus"))
		.andExpect(jsonPath("$.description").value("New Description is Iphone 7 Plus 256gb Black Piano"))
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
