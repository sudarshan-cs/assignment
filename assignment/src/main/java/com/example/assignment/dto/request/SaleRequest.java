package com.example.assignment.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SaleRequest {

    @NotNull
    @NotEmpty
    @Valid
    private List<SaleDetail> sales;

    public void mergeDuplicateProductQuantities() {
        Map<Long, SaleDetail> productMap = new HashMap<>();

        for (SaleDetail saleDetail : sales) {
            if (productMap.containsKey(saleDetail.getProductId())) {
                // Merge the quantities if the productId already exists
                SaleDetail existingSaleDetail = productMap.get(saleDetail.getProductId());
                existingSaleDetail.setQuantity(existingSaleDetail.getQuantity() + saleDetail.getQuantity());
            } else {
                // Add new product to the map
                productMap.put(saleDetail.getProductId(), saleDetail);
            }
        }

        // Update the sales list with the merged results
        sales.clear();
        sales.addAll(productMap.values());
    }



    @Data
    public static class SaleDetail {

        @NotNull
        private Long productId;

        @NotNull
        private int quantity;

        // Override equals and hashCode to compare based on productId
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SaleDetail that = (SaleDetail) o;

            return productId != null ? productId.equals(that.productId) : that.productId == null;
        }

        @Override
        public int hashCode() {
            return productId != null ? productId.hashCode() : 0;
        }
    }

}
