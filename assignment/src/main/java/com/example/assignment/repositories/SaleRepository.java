package com.example.assignment.repositories;

import com.example.assignment.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findBySaleDate(LocalDate saleDate);

    @Query("SELECT SUM(s.totalPricePerUnit * s.quantity) FROM Sale s WHERE s.saleDate = :saleDate")
    BigDecimal findTotalRevenueByDate(LocalDate saleDate);

    @Query("SELECT SUM(s.totalPricePerUnit * s.quantity) FROM Sale s WHERE MONTH(s.saleDate) = :month AND YEAR(s.saleDate) = :year")
    BigDecimal findTotalRevenueByMonthAndYear(@Param("month") int month, @Param("year") int year);



    @Query("SELECT SUM(s.totalPricePerUnit * s.quantity) FROM Sale s WHERE YEAR(s.saleDate) = :year")
    BigDecimal findTotalRevenueByYear(@Param("year") int year);

}
