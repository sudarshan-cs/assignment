package com.example.assignment.services.impl;

import com.example.assignment.dto.request.ProductRequest;
import com.example.assignment.dto.response.DuplicateProduct;
import com.example.assignment.entity.Category;
import com.example.assignment.entity.Product;
import com.example.assignment.repositories.CategoryRepository;
import com.example.assignment.repositories.ProductRepository;
import com.example.assignment.services.ProductService;
import com.example.assignment.util.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
@Autowired
    private ProductRepository productRepository;
@Autowired
private CategoryRepository categoryRepository;
    @Override
    public ResponseEntity<Object> createProductsForCategory(ProductRequest productRequest) {
        Optional<Category> category = categoryRepository.findById(productRequest.getCategoryId());
        if (category.isEmpty())
            return ResultUtil.generateResponse("Category not found.",HttpStatus.NOT_FOUND);


        List<Product> productsToSave = new ArrayList<>();
        Set<DuplicateProduct> duplicateProducts=new HashSet<>();
        productRequest.removeDuplicateProducts();

        for (ProductRequest.ProductDetail productDetail : productRequest.getProducts()) {
            if (isProductAlreadyExistForCategoryInDB(productRequest.getCategoryId(),productDetail.getProductName()))
            {
                duplicateProducts.add(new DuplicateProduct(productRequest.getCategoryId(),productDetail.getProductName()));
                continue;
            }
            Product product = new Product();
            BeanUtils.copyProperties(productDetail,product);
            product.setCategory(category.get());
            productsToSave.add(product);
        }
        if (!productsToSave.isEmpty()) {

            try
            {
                productRepository.saveAll(productsToSave);
            } catch (Exception e) {
                return ResultUtil.generateResponse("unable to create products, retry with valid price(price between 0.00 to 99999999.99, can only have 2 digit after decimal point, refer this type DECIMAL(10, 2))", HttpStatus.NOT_ACCEPTABLE);
            }

        }

        if (!duplicateProducts.isEmpty())
            return ResultUtil.generateResponse("duplicate product for same category are discarded"+duplicateProducts, HttpStatus.CREATED);

         // Save all products in one go

        return ResultUtil.generateResponse("Products created successfully.",HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Object> getAllProducts(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);

        if (products.isEmpty()) {
            return ResultUtil.generateResponse("No products found for the category.", HttpStatus.NOT_FOUND);
        }
        return ResultUtil.generateResponse(products, HttpStatus.OK);
    }

    public boolean isProductAlreadyExistForCategoryInDB(Long categoryId, String productName)
    {
        return productRepository.findByCategoryIdAndProductName(categoryId,productName).isPresent();
    }

}
