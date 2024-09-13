package com.example.assignment.dto.response;

import com.example.assignment.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SaleResponse {
    private BigDecimal pricePerUnitAfterTax;
    private BigDecimal totalPriceAfterTax;
    private LocalDate saleDate;
    private String productName;
    private BigDecimal productPrice;
    private String categoryName;
    private BigDecimal gstRate;
    private Integer quantity;
}
