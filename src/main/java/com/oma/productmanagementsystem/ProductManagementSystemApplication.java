package com.oma.productmanagementsystem;

import com.oma.productmanagementsystem.entities.Product;
import com.oma.productmanagementsystem.entities.ProductUser;
import com.oma.productmanagementsystem.repositories.ProductRepository;
import com.oma.productmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class ProductManagementSystemApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProductManagementSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ProductUser user = new ProductUser();
        user.setUserId("1");
        user.setEmail("oma@email.com");
        user.setFirstName("Oma");
        user.setLastName("Salifu");
        user.setPassword("password");
        userRepository.save(user);

        ProductUser user2 = new ProductUser();
        user2.setUserId("2");
        user2.setEmail("oma2@email.com");
        user2.setFirstName("Oma2");
        user2.setLastName("Salifu2");
        user2.setPassword("password2");
        userRepository.save(user2);

        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Laptop");
        product.setBrand("Dell");
        product.setPrice(20.5);
        product.setDescription("Very good laptop");
        product.setImage("abc");
        productRepository.save(product);

        Product product2 = new Product();
        product2.setProductId("2");
        product2.setProductName("New Laptop");
        product2.setBrand("Hp");
        product2.setPrice(22);
        product2.setDescription("Very nice laptop");
        product2.setImage("def");

        productRepository.save(product2);
    }
}
