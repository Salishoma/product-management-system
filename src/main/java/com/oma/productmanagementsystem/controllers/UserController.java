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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
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
    @PreAuthorize("hasAuthority('profile:write')")
    public ResponseEntity<List<UserResponseModel>> getUsers() {
        List<UserResponseModel> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('profile:write') or principal == @emailSearch.getEmailFromId(#userId)")
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
    @PreAuthorize("hasAuthority('profile:write') or principal == @emailSearch.getEmailFromId(#userId)")
    public ResponseEntity<UserResponseModel> updateUser(@PathVariable final String userId, UserRequestModel userRequestModel) {
        UserResponseModel user = userService.updateUser(userId, userRequestModel);
        return user == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('profile:delete') or principal == @emailSearch.getEmailFromId(#userId)")
    public ResponseEntity<String> deleteUser(@PathVariable final String userId) {
        ;
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLogin authenticationRequest) {
        try {
            String email = authenticationRequest.getEmail();
            String password = authenticationRequest.getPassword();
            Authentication authentication = authenticate(email, password);

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
