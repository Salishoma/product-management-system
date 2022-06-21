package com.oma.productmanagementsystem.dtos;

import lombok.Data;

@Data
public class ProductRequestModel {

    private String productName;

    private String image;

    private String brand;

    private String description;

    private double price;
}
