package com.oma.productmanagementsystem.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "product_user")
public class ProductUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String userId;

    private String firstName;
    private String lastName;

    private String password;
    private String email;
}
