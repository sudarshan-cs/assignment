package com.example.assignment.services.impl;

import com.example.assignment.dto.request.CategoryRequest;
import com.example.assignment.entity.Category;
import com.example.assignment.repositories.CategoryRepository;
import com.example.assignment.services.CategoryService;
import com.example.assignment.util.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
@Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<Object> createCategory(CategoryRequest categoryRequest) {
        Set<String> duplicateCategories= new HashSet<>();
        List<Category> categoriesToSave = new ArrayList<>();
        System.out.println(categoryRequest);
        categoryRequest.removeDuplicateCategories();
        for (CategoryRequest.CategoryDetail categoryDetail : categoryRequest.getCategories())
        {
            if (isCategoryAlreadyExistInDB(categoryDetail.getCategoryName()))
            {
                duplicateCategories.add(categoryDetail.getCategoryName());
                continue;
            }
            Category category = new Category();
            BeanUtils.copyProperties(categoryDetail,category);
            categoriesToSave.add(category);
        }
        if (!categoriesToSave.isEmpty()) {

            try
            {
                categoryRepository.saveAll(categoriesToSave);
            } catch (Exception e) {
                return ResultUtil.generateResponse("unable to create category, retry with valid gstRate(gstRate between 0.00 to 100.00, can only have 2 digit after decimal point,refer this type DECIMAL(5, 2))", HttpStatus.NOT_ACCEPTABLE);
            }

        }

        if (!duplicateCategories.isEmpty())
            return ResultUtil.generateResponse("duplicate categories are discarded"+ Arrays.toString(duplicateCategories.toArray()), HttpStatus.CREATED);

        return ResultUtil.generateResponse("Categories created successfully.", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> getAllCategories() {
        List<Category> categories = categoryRepository.findAll(); // Retrieve all categories

        if (categories.isEmpty()) {
            // Optional: Return 204 if no categories are found
            return ResultUtil.generateResponse("No categories found.", HttpStatus.NO_CONTENT);
        }

        // Return the list of categories with a 200 OK status
        return ResultUtil.generateResponse(categories, HttpStatus.OK);
    }


    public boolean isCategoryAlreadyExistInDB(String categoryName)
    {
        return categoryRepository.findByCategoryName(categoryName).isPresent();
    }



}
