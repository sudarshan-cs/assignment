package com.example.assignment.services;

import com.example.assignment.dto.request.CategoryRequest;
import com.example.assignment.entity.Category;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface CategoryService {
    ResponseEntity<Object> createCategory(CategoryRequest categoryRequest);
    ResponseEntity<Object> getAllCategories();
}
