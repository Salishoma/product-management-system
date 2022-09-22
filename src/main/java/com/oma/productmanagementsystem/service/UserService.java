package com.oma.productmanagementsystem.service;

import com.oma.productmanagementsystem.dtos.UserRequestModel;
import com.oma.productmanagementsystem.dtos.UserResponseModel;
import com.oma.productmanagementsystem.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserResponseModel createUser(UserRequestModel userRequestModel);
    UserResponseModel getUser(String userId);
    UserResponseModel updateUser(String userId, UserRequestModel userRequestModel);
    String deleteUser(String userId);
    List<UserResponseModel> getUsers();
    UserEntity findByEmail(String email);

}
