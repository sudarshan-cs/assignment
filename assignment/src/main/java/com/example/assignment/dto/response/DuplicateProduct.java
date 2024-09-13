package com.example.assignment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateProduct {
    private long categoryId;
    private String productName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DuplicateProduct)) return false;
        DuplicateProduct that = (DuplicateProduct) o;
        return categoryId == that.categoryId &&
                Objects.equals(productName, that.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, productName);
    }
}
