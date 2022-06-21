package com.oma.productmanagementsystem.controllers;

import com.oma.productmanagementsystem.dtos.ProductRequestModel;
import com.oma.productmanagementsystem.dtos.ProductResponseModel;
import com.oma.productmanagementsystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @RequestMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ProductResponseModel> getProduct(@PathVariable String productId) {
        ProductResponseModel responseModel = productService.getProduct(productId);
        return responseModel == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(responseModel);
    }

    @RequestMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<List<ProductResponseModel>> getProducts() {
        List<ProductResponseModel> responseModel = productService.getProducts();
        return ResponseEntity.ok(responseModel);
    }

    @RequestMapping(value = "", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<ProductResponseModel> createProduct(@RequestBody ProductRequestModel requestModel) {
        ProductResponseModel responseModel = productService.createProduct(requestModel);
        return ResponseEntity.ok(responseModel);
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<ProductResponseModel> updateProduct(@RequestBody ProductRequestModel requestModel,
            @PathVariable String productId
    ) {
        ProductResponseModel responseModel = productService.updateProduct(requestModel, productId);
        return responseModel == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(responseModel);
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
        String response = productService.deleteProduct(productId);
        return ResponseEntity.ok(response);
    }
}
