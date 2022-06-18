package com.oma.productmanagementsystem;

import com.oma.productmanagementsystem.entities.ProductUser;
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
        System.out.println("=========> Users: " + userRepository.findAll());
        System.out.println("=======>Users successfully saved");
    }
}
