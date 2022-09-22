package com.oma.productmanagementsystem.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oma.productmanagementsystem.dtos.UserRequestModel;
import com.oma.productmanagementsystem.dtos.UserResponseModel;
import com.oma.productmanagementsystem.entities.UserEntity;
import com.oma.productmanagementsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(value="abc", authorities = {"profile:write"})
public class UserEntityITTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    UserEntity userEntity;

    UserEntity user2;

    ArrayList<UserEntity> list;

    @BeforeEach
    public void setUp() {

        list = spy(ArrayList.class);
        userEntity = new UserEntity();

        userEntity.setEncryptedPassword("abc");
        userEntity.setUserId("3");
        userEntity.setFirstName("oma");
        userEntity.setLastName("shadu");
        userEntity.setEmail("oma@shadu.com");

        user2 = new UserEntity();
        user2.setUserId("2");
        user2.setEmail("oma2@email.com");
        user2.setFirstName("Oma2");
        user2.setLastName("Salifu2");
        user2.setEncryptedPassword("password2");
        list.add(userEntity);
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
        mockMvc.perform(get("/users/{id}",3))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("3"))
                .andExpect(jsonPath("$.firstName", is("oma")))
                .andExpect(jsonPath("$.lastName", is("shadu")))
                .andExpect(jsonPath("$.email", is("oma@shadu.com")));
    }

    @Test
    public void getUsers() throws Exception {

        List<UserResponseModel> users = findUsers();

        when(userService.getUsers())
                .thenReturn(users);
        mockMvc.perform(get("/users"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].firstName", is("oma")))
                .andExpect(jsonPath("$[1].lastName", is("Salifu2")))
                .andExpect(jsonPath("$[1].email", is("oma2@email.com")));
    }

    @Test
    public void createUser() throws Exception {

        UserRequestModel model = requestModel("4");
        UserResponseModel responseModel = createdUser(model);
        when(userService.createUser((any(UserRequestModel.class))))
                .thenReturn(responseModel);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsBytes(responseModel)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Oma4")))
                .andExpect(jsonPath("$.lastName", is("Salifu4")))
                .andExpect(jsonPath("$.email", is("oma4@email")));
    }

    @Test
    public void updateUser() throws Exception {

        UserRequestModel model = requestModel("2");
        UserResponseModel responseModel = updatedUser(user2.getUserId(), model);
        when(userService.updateUser(any(String.class), any(UserRequestModel.class)))
                .thenReturn(responseModel);

        mockMvc.perform(put("/users/{id}", 2))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Oma2")))
                .andExpect(jsonPath("$.lastName", is("Salifu2")))
                .andExpect(jsonPath("$.email", is("oma2@email")));
    }

    @Test
    @WithMockUser(value="abc", authorities = {"profile:delete"})
    public void removeUser() throws Exception {

        doNothing().when(userService).deleteUser(any(String.class));

        mockMvc.perform(delete("/users/{id}", "2"))
                .andExpect(status().isNoContent());
    }

    private UserResponseModel getUserResponseModel() {
        return UserResponseModel.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();
    }

    private UserResponseModel createdUser(UserRequestModel requestModel) {
        return new ModelMapper().map(requestModel, UserResponseModel.class);
    }

    private UserRequestModel requestModel(String num) {
        UserRequestModel model = new UserRequestModel();
        model.setFirstName("Oma" + num);
        model.setLastName("Salifu" + num);
        model.setEmail("oma" + num + "@email");
        model.setPassword("newPassword" + num);
        return model;
    }

    private UserResponseModel updatedUser(String userId, UserRequestModel model) {
        int ind = 0;
        UserEntity user;
        for (int i = 0; i < list.size(); i++) {
            if (userId.equals(user2.getUserId())) {
                ind = i;
                break;
            }
        }
        ModelMapper mapper = new ModelMapper();
        user = mapper.map(model, UserEntity.class);
        list.set(ind, user);
        return mapper.map(model, UserResponseModel.class);
    }

    private List<UserResponseModel> findUsers() {
        return list.stream().map(userEntity1 ->
                new ModelMapper().map(userEntity1, UserResponseModel.class))
                .collect(Collectors.toList());
    }
}
