package com.oma.productmanagementsystem.config;

import com.oma.productmanagementsystem.entities.UserEntity;
import com.oma.productmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailSearch {

    @Autowired
    UserRepository repository;

    public String getEmailFromId(String userId){
        UserEntity entity = repository.findById(userId).orElse(null);
        if(entity != null){
            return entity.getEmail();
        }
        return null;
    }
}