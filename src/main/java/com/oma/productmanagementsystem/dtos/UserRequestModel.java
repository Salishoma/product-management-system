package com.oma.productmanagementsystem.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequestModel {

    private String userId;

    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
