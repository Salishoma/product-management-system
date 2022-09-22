package com.oma.productmanagementsystem.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oma.productmanagementsystem.dtos.ProductRequestModel;
import com.oma.productmanagementsystem.dtos.ProductResponseModel;
import com.oma.productmanagementsystem.entities.Product;
import com.oma.productmanagementsystem.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ProductITTest {

    @Autowired
    private WebApplicationContext context;

    Product product;

    List<Product> list;

    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @BeforeEach
    public void setUp() {
        list = spy(ArrayList.class);
        product = new Product();

        product.setProductId("3");
        product.setProductName("Phone");
        product.setBrand("Infinix");
        product.setImage("ghi");
        product.setPrice(15);
        product.setDescription("I love this");

        Product product1 = new Product();
        product1.setProductId("4");
        product1.setProductName("Phone");
        product1.setBrand("Techno");
        product1.setImage("jkl");
        product1.setPrice(13);
        product1.setDescription("It looks good");
        list.add(product);
        list.add(product1);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void findProduct() throws Exception {
        when(productService.getProduct(any(String.class)))
                .thenReturn(getProductResponseModel());
        mockMvc.perform(get("/products/{id}", 3).with(user("user")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is("3")))
                .andExpect(jsonPath("$.productName", is("Phone")))
                .andExpect(jsonPath("$.brand", is("Infinix")))
                .andExpect(jsonPath("$.image", is("ghi")))
                .andExpect(jsonPath("$.price", is(15.0)))
                .andExpect(jsonPath("$.description", is("I love this")));
    }

    @Test
    public void getProducts() throws Exception {
        List<ProductResponseModel> products = findProducts();
        when(productService.getProducts())
                .thenReturn(products);
        mockMvc.perform(get("/products").with((user("user"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[1].productId", is("4")))
                .andExpect(jsonPath("$[0].brand", is("Infinix")))
                .andExpect(jsonPath("$[1].description", is("It looks good")));
    }

    @Test
    @WithMockUser(value="abc", authorities = {"profile:write"})
    public void createProduct() throws Exception {
        ProductRequestModel requestModel = createNewProduct();
        ProductResponseModel responseModel = sendProductResponse(requestModel);
        ObjectMapper mapper = new ObjectMapper();
        when(productService.createProduct(any(ProductRequestModel.class)))
                .thenReturn(responseModel);
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(requestModel)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("five"))
                .andExpect(jsonPath("$.brand").value("pro5"))
                .andExpect(jsonPath("$.image", is("asd")))
                .andExpect(jsonPath("$.description", is("fifth product")));
    }

    private ProductRequestModel createNewProduct() {
        Product product = new Product();
        product.setProductId("5");
        product.setProductName("five");
        product.setDescription("fifth product");
        product.setImage("asd");
        product.setBrand("pro5");
        product.setPrice(20.3);
        return new ModelMapper().map(product, ProductRequestModel.class);
    }
    private ProductResponseModel sendProductResponse(ProductRequestModel model) {
        return new ModelMapper().map(model, ProductResponseModel.class);
    }

    private List<ProductResponseModel> findProducts() {
        return list.stream().map(product1 ->
                new ModelMapper().map(product1, ProductResponseModel.class))
                .collect(Collectors.toList());
    }

    private ProductResponseModel getProductResponseModel() {
        return ProductResponseModel.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .brand(product.getBrand())
                .image(product.getImage())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }
}
