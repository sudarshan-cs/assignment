package com.example.assignment.services;


import com.example.assignment.dto.request.SaleRequest;
import com.example.assignment.entity.Sale;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SaleService {
    ResponseEntity<Object> registerSales(SaleRequest saleRequest);
    ResponseEntity<Object> getSalesForDay(LocalDate saleDate);
    ResponseEntity<Object> getTotalRevenueForYear(int year);
    ResponseEntity<Object> getTotalRevenueForMonthAndYear(int month,int year);
    ResponseEntity<Object> getTotalRevenueForDay(LocalDate saleDate);
}