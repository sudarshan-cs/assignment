package com.example.assignment.services;

import com.example.assignment.dto.request.ProductRequest;
import com.example.assignment.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<Object> createProductsForCategory(ProductRequest productRequest);
    ResponseEntity<Object> getAllProducts( Long categoryId);
}

