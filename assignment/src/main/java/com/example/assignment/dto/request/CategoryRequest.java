package com.example.assignment.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
public class CategoryRequest {

    @NotNull
    @NotEmpty
    @Valid
    private List<CategoryDetail> categories;

    public void removeDuplicateCategories() {
        Set<CategoryDetail> uniqueCategories = new HashSet<>(categories);
        categories.clear();
        categories.addAll(uniqueCategories);
    }

    @Data
    public static class CategoryDetail {
        @NotNull
        private String categoryName;

        @NotNull
        @DecimalMin(value = "0.0", message = "GST must be between 0 and 100")
        @DecimalMax(value = "100.00", message = "GST must be between 0 and 100")
        private BigDecimal gstRate;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CategoryDetail that = (CategoryDetail) o;

            return Objects.equals(categoryName, that.categoryName);
        }

        @Override
        public int hashCode() {
            return categoryName != null ? categoryName.hashCode() : 0;
        }
    }



}
