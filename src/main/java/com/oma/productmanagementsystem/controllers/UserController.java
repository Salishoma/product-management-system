package com.oma.productmanagementsystem.controllers;

import com.oma.productmanagementsystem.dtos.UserRequestModel;
import com.oma.productmanagementsystem.dtos.UserResponseModel;
import com.oma.productmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseModel>> getUsers() {
        List<UserResponseModel> users = userService.getUsers();
        System.out.println("=========>Users: " + users);
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> getUser(@PathVariable String userId) {
        System.out.println("user id is: " + userId);

        UserResponseModel user = userService.getUser(userId);
        System.out.println("Response entity: " + ResponseEntity.ok(user));
        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> createUser(@RequestBody UserRequestModel userRequestModel) {
        UserResponseModel user = userService.createUser(userRequestModel);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<UserResponseModel> updateUser(@PathVariable final String userId, UserRequestModel userRequestModel) {
        UserResponseModel user = userService.updateUser(userId, userRequestModel);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity.HeadersBuilder<?> deleteUser(@PathVariable final String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent();
    }
}
