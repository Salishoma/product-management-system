package com.oma.productmanagementsystem.service;

import com.oma.productmanagementsystem.dtos.ProductRequestModel;
import com.oma.productmanagementsystem.dtos.ProductResponseModel;

import java.util.List;

public interface ProductService {

    ProductResponseModel createProduct(ProductRequestModel productRequestModel);
    ProductResponseModel updateProduct(ProductRequestModel productRequestModel, String productId);
    ProductResponseModel getProduct(String productId);
    List<ProductResponseModel> getProducts();
    String deleteProduct(String productId);
}
