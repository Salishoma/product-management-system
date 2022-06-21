package com.oma.productmanagementsystem.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Product {

    @Id
    private String productId;

    @Column
    private String productName;

    @Column
    private String image;

    @Column
    private String brand;

    @Column
    private String description;

    @Column
    private double price;


}
