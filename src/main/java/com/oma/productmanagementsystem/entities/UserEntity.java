package com.oma.productmanagementsystem.entities;

import com.oma.productmanagementsystem.enums.ApplicationUserRole;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    private ApplicationUserRole userRole;

}
