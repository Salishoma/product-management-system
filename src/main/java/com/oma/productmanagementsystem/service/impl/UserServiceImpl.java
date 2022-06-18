package com.oma.productmanagementsystem.service.impl;

import com.oma.productmanagementsystem.dtos.UserRequestModel;
import com.oma.productmanagementsystem.dtos.UserResponseModel;
import com.oma.productmanagementsystem.entities.ProductUser;
import com.oma.productmanagementsystem.repositories.UserRepository;
import com.oma.productmanagementsystem.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserResponseModel createUser(UserRequestModel userRequestModel) {
        ModelMapper mapper = new ModelMapper();
        ProductUser user = mapper.map(userRequestModel, ProductUser.class);
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        userRepository.save(user);
        UserResponseModel userResponseModel = mapper.map(userRequestModel, UserResponseModel.class);

        userResponseModel.setUserId(userId);
        return userResponseModel;
    }

    @Override
    public UserResponseModel getUser(String userId) {
        ProductUser user = userRepository.findById(userId)
                .orElse(null);
        if (user == null) {
            return null;
        }
        ModelMapper mapper = new ModelMapper();
        return mapper.map(user, UserResponseModel.class);
    }

    @Override
    public UserResponseModel updateUser(String userId, UserRequestModel userRequestModel) {
        return null;
    }

    @Override
    public UserRequestModel deleteUser(String userId) {
        return null;
    }

    @Override
    public List<UserResponseModel> getUsers() {
        System.out.println("users: " + userRepository.findAll());
        return userRepository.findAll()
                .stream().map(user ->
                    new ModelMapper().map(user, UserResponseModel.class)
                ).collect(Collectors.toList());
    }
}
