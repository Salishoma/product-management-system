package com.oma.productmanagementsystem.entities;

import com.oma.productmanagementsystem.enums.ApplicationUserRole;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String userId;

    private String firstName;
    private String lastName;

    private String encryptedPassword;
    private String email;

    @Enumerated(EnumType.STRING)
    private ApplicationUserRole userRole;

}
