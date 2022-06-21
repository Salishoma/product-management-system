package com.oma.productmanagementsystem.service.impl;

import com.oma.productmanagementsystem.dtos.ProductRequestModel;
import com.oma.productmanagementsystem.dtos.ProductResponseModel;
import com.oma.productmanagementsystem.entities.Product;
import com.oma.productmanagementsystem.repositories.ProductRepository;
import com.oma.productmanagementsystem.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public ProductResponseModel createProduct(ProductRequestModel productRequestModel) {
        ModelMapper mapper = new ModelMapper();
        Product product = mapper.map(productRequestModel, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        productRepository.save(product);
        return mapper.map(product, ProductResponseModel.class);
    }

    @Override
    public ProductResponseModel updateProduct(ProductRequestModel productRequestModel, String productId) {
        ModelMapper mapper = new ModelMapper();
        Product product = productRepository.findById(productId)
                .orElse(null);
        if (product == null) {
            return null;
        }
        Product updatedProduct = mapper.map(productRequestModel, Product.class);
        productRepository.save(updatedProduct);

        return mapper.map(updatedProduct, ProductResponseModel.class);
    }

    @Override
    public ProductResponseModel getProduct(String productId) {
        ModelMapper mapper = new ModelMapper();
        Product product = productRepository.findById(productId)
                .orElse(null);
        if (product == null) {
            return null;
        }

        return mapper.map(product, ProductResponseModel.class);
    }

    @Override
    public List<ProductResponseModel> getProducts() {
        return productRepository.findAll()
                .stream().map(product ->
                        new ModelMapper().map(product, ProductResponseModel.class)
                        ).collect(Collectors.toList());
    }

    @Override
    public String deleteProduct(String productId) {
        return "";
    }
}
