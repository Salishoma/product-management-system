package com.oma.productmanagementsystem.service;

import com.oma.productmanagementsystem.dtos.UserRequestModel;
import com.oma.productmanagementsystem.dtos.UserResponseModel;

import java.util.List;

public interface UserService {
    UserResponseModel createUser(UserRequestModel userRequestModel);
    UserResponseModel getUser(String userId);
    UserResponseModel updateUser(String userId, UserRequestModel userRequestModel);
    UserRequestModel deleteUser(String userId);
    List<UserResponseModel> getUsers();

}
