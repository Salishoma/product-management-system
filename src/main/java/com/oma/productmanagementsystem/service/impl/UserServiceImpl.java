package com.oma.productmanagementsystem.service.impl;

import com.oma.productmanagementsystem.dtos.UserRequestModel;
import com.oma.productmanagementsystem.dtos.UserResponseModel;
import com.oma.productmanagementsystem.entities.UserEntity;
import com.oma.productmanagementsystem.repositories.UserRepository;
import com.oma.productmanagementsystem.security.SecurityUser;
import com.oma.productmanagementsystem.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.oma.productmanagementsystem.enums.ApplicationUserRole.ADMIN;
import static com.oma.productmanagementsystem.enums.ApplicationUserRole.USER;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseModel createUser(UserRequestModel userRequestModel) {
        ModelMapper mapper = new ModelMapper();
        UserEntity user = mapper.map(userRequestModel, UserEntity.class);
        user.setEncryptedPassword(passwordEncoder.encode(userRequestModel.getPassword()));
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        userRepository.save(user);

        return mapper.map(user, UserResponseModel.class);
    }

    @Override
    public UserResponseModel getUser(String userId) {
        UserEntity user = userRepository.findById(userId)
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
        return userRepository.findAll()
                .stream().map(user ->
                    new ModelMapper().map(user, UserResponseModel.class)
                ).collect(Collectors.toList());
    }

    @Override
    public UserEntity findByEmail(String username){
        return userRepository.findByEmail(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if(userEntity != null) {
            Collection<? extends GrantedAuthority> authorities = userEntity.getUserRole().equals(ADMIN) ?
                    ADMIN.getGrantedAuthorities() : USER.getGrantedAuthorities();

            return new SecurityUser(userEntity, authorities);
        }
        return null;
    }
}
