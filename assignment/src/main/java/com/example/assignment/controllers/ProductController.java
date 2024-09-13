package com.example.assignment.controllers;

import com.example.assignment.dto.request.ProductRequest;
import com.example.assignment.security.JwtTokenUtil;
import com.example.assignment.services.ProductService;
import com.example.assignment.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private JwtTokenUtil jwtUtil;
    private static final String BEARER ="Bearer " ;
    // POST - Add a new product
    @PostMapping("/admin/products")
    public ResponseEntity<Object> addProduct(HttpServletRequest httpServletRequest, @RequestBody ProductRequest productRequest) {
        String role= jwtUtil.getUsernameFromToken(httpServletRequest.getHeader(AUTHORIZATION).replace(BEARER, ""));
        if (!"admin".equalsIgnoreCase(role))
            return ResultUtil.generateResponse("\"Unauthorized\", \"only admin can access this resource\"", HttpStatus.UNAUTHORIZED);

        return productService.createProductsForCategory(productRequest);
    }

    // GET - Get products by category ID
    @GetMapping("/all/product/{categoryId}")
    public ResponseEntity<Object> getProductsByCategoryId(@PathVariable Long categoryId) {
        return productService.getAllProducts(categoryId);
    }
}