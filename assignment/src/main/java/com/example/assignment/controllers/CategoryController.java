package com.example.assignment.controllers;


import com.example.assignment.dto.request.CategoryRequest;
import com.example.assignment.entity.Category;
import com.example.assignment.security.JwtTokenUtil;
import com.example.assignment.services.CategoryService;
import com.example.assignment.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JwtTokenUtil jwtUtil;
    private static final String BEARER ="Bearer " ;

    @PostMapping("/admin/categories")
    public ResponseEntity<Object> createCategory(HttpServletRequest httpServletRequest, @RequestBody CategoryRequest categoryRequest) {

        String role= jwtUtil.getUsernameFromToken(httpServletRequest.getHeader(AUTHORIZATION).replace(BEARER, ""));
        if (!"admin".equalsIgnoreCase(role))
            return ResultUtil.generateResponse("\"Unauthorized\", \"only admin can access this resource\"",HttpStatus.UNAUTHORIZED);
        return categoryService.createCategory(categoryRequest);
    }

    @GetMapping("/all/categories")
    public ResponseEntity<Object> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
