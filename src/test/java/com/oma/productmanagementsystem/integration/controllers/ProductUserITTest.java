package com.oma.productmanagementsystem.integration.controllers;

import com.oma.productmanagementsystem.dtos.UserResponseModel;
import com.oma.productmanagementsystem.entities.ProductUser;
import com.oma.productmanagementsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ProductUserITTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    ProductUser productUser;

    List<ProductUser> list;

    @BeforeEach
    public void setUp() {

        list = spy(ArrayList.class);
        productUser = new ProductUser();

        productUser.setPassword("abc");
        productUser.setUserId("3");
        productUser.setFirstName("oma");
        productUser.setLastName("shadu");
        productUser.setEmail("oma@shadu.com");

        ProductUser user2 = new ProductUser();
        user2.setUserId("2");
        user2.setEmail("oma2@email.com");
        user2.setFirstName("Oma2");
        user2.setLastName("Salifu2");
        user2.setPassword("password2");
        list.add(productUser);
        list.add(user2);

                mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }

    @Test
    public void findUser() throws Exception {
        when(userService.getUser(any(String.class)))
                .thenReturn(getUserResponseModel());
        mockMvc.perform(get("/api/users/3").with((user("user"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is("3")))
                .andExpect(jsonPath("$.firstName", is("oma")))
                .andExpect(jsonPath("$.lastName", is("shadu")))
                .andExpect(jsonPath("$.email", is("oma@shadu.com")));
    }

    @Test
    public void getUsers() throws Exception {

        List<UserResponseModel> users = findUsers();

        when(userService.getUsers())
                .thenReturn(users);
        mockMvc.perform(get("/api/users/").with((user("user"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].firstName", is("oma")))
                .andExpect(jsonPath("$[1].lastName", is("Salifu2")))
                .andExpect(jsonPath("$[1].email", is("oma2@email.com")));
    }

    private UserResponseModel getUserResponseModel() {
        System.out.println("Id is: " + productUser.getUserId());
        return UserResponseModel.builder()
                .userId(productUser.getUserId())
                .email(productUser.getEmail())
                .firstName(productUser.getFirstName())
                .lastName(productUser.getLastName())
                .build();
    }

    private List<UserResponseModel> findUsers() {
        return list.stream().map(productUser1 ->
            new ModelMapper().map(productUser1, UserResponseModel.class)
        ).collect(Collectors.toList());
    }
}
