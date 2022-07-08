package com.oma.productmanagementsystem.controllers;

import com.oma.productmanagementsystem.dtos.UserLogin;
import com.oma.productmanagementsystem.dtos.UserRequestModel;
import com.oma.productmanagementsystem.dtos.UserResponseModel;
import com.oma.productmanagementsystem.entities.UserEntity;
import com.oma.productmanagementsystem.service.UserService;
import com.oma.productmanagementsystem.utils.JwtResponse;
import com.oma.productmanagementsystem.utils.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
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
        return user == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(user);
    }

    @RequestMapping(value = "", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> createUser(@RequestBody UserRequestModel userRequestModel) {
        UserResponseModel user = userService.createUser(userRequestModel);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<UserResponseModel> updateUser(@PathVariable final String userId, UserRequestModel userRequestModel) {
        UserResponseModel user = userService.updateUser(userId, userRequestModel);
        return user == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity.HeadersBuilder<?> deleteUser(@PathVariable final String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent();
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLogin authenticationRequest) {
        try {
            Authentication authentication = authenticate(authenticationRequest.getEmail(),
                    authenticationRequest.getPassword());

            final UserDetails userDetails =
                    userService.loadUserByUsername(authenticationRequest.getEmail());

            UserEntity userEntity = userService.findByEmail(authenticationRequest.getEmail());
            UserResponseModel userResponseModel = new ModelMapper()
                    .map(userEntity, UserResponseModel.class);

            final String token = jwtTokenUtil.generateToken(userDetails, authentication);
            return ResponseEntity.ok(new JwtResponse<>(userResponseModel, token));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
