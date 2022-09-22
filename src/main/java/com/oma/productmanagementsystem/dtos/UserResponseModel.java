package com.oma.productmanagementsystem.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseModel {

    private String userId;

    private String firstName;
    private String lastName;
    private String email;
}
