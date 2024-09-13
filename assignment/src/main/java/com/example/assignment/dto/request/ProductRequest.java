package com.example.assignment.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

@Data
public class ProductRequest {

    @NotNull
    private Long categoryId;

    @NotNull
    @Valid
    @NotEmpty
    private List<ProductDetail> products;

    public void removeDuplicateProducts() {
        Set<ProductDetail> uniqueProducts = new HashSet<>(products);
        products.clear();
        products.addAll(uniqueProducts);
    }
    @Data
    public static class ProductDetail {
        @NotNull
        private String productName;

        @NotNull
        @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
        @DecimalMax(value = "99999999.99", message = "Price must not exceed 99999999.99")
        private BigDecimal price;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ProductDetail that = (ProductDetail) o;

            return Objects.equals(productName, that.productName);
        }

        @Override
        public int hashCode() {
            return productName != null ? productName.hashCode() : 0;
        }

    }


}
