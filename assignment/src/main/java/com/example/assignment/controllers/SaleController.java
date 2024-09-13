package com.example.assignment.controllers;

import com.example.assignment.dto.request.SaleRequest;
import com.example.assignment.security.JwtTokenUtil;
import com.example.assignment.services.SaleService;
import com.example.assignment.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
public class SaleController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private JwtTokenUtil jwtUtil;
    private static final String BEARER ="Bearer " ;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // POST - Create a new sale
    @PostMapping("/user/sales")
    public ResponseEntity<Object> registerSales(HttpServletRequest httpServletRequest, @RequestBody SaleRequest saleRequest) {
        String role= jwtUtil.getUsernameFromToken(httpServletRequest.getHeader(AUTHORIZATION).replace(BEARER, ""));
        if (!"user".equalsIgnoreCase(role))
            return ResultUtil.generateResponse("\"Unauthorized\", \"only user can register sale\"",HttpStatus.UNAUTHORIZED);

        return saleService.registerSales(saleRequest);
    }

    // GET - Get sales for a specific day
    @GetMapping("/admin/sales/day/{saleDate}")
    public ResponseEntity<Object> getSalesForDay(HttpServletRequest httpServletRequest,@PathVariable String saleDate) {
        String role= jwtUtil.getUsernameFromToken(httpServletRequest.getHeader(AUTHORIZATION).replace(BEARER, ""));
        if (!"admin".equalsIgnoreCase(role))
            return ResultUtil.generateResponse("\"Unauthorized\", \"only admin see sales\"",HttpStatus.UNAUTHORIZED);

        LocalDate date = LocalDate.parse(saleDate);  // Parsing the date string to LocalDate
        return saleService.getSalesForDay(date);
    }

    // GET - Calculate total revenue for a specific year
    @GetMapping("/admin/sales/revenue/year/{year}")
    public ResponseEntity<Object> getTotalRevenueForYear(HttpServletRequest httpServletRequest,@PathVariable String year) {
        String role= jwtUtil.getUsernameFromToken(httpServletRequest.getHeader(AUTHORIZATION).replace(BEARER, ""));
        if (!"admin".equalsIgnoreCase(role))
            return ResultUtil.generateResponse("\"Unauthorized\", \"only admin see sales\"",HttpStatus.UNAUTHORIZED);

        try {
            int yearInt = Integer.parseInt(year);
            if (yearInt < 1900 || yearInt > LocalDate.now().getYear()) {
                return ResultUtil.generateResponse("Invalid year. Year must be between 1900 and the current year.", HttpStatus.BAD_REQUEST);
            }
            return saleService.getTotalRevenueForYear(yearInt);
        } catch (NumberFormatException e) {
            return ResultUtil.generateResponse("Year must be a valid integer.", HttpStatus.BAD_REQUEST);
        }
    }

    // GET - Calculate total revenue for a specific month and year
    @GetMapping("/admin/sales/revenue/month/{month}/year/{year}")
    public ResponseEntity<Object> getTotalRevenueForMonthAndYear(HttpServletRequest httpServletRequest,@PathVariable String month, @PathVariable String year) {
        String role= jwtUtil.getUsernameFromToken(httpServletRequest.getHeader(AUTHORIZATION).replace(BEARER, ""));
        if (!"admin".equalsIgnoreCase(role))
            return ResultUtil.generateResponse("\"Unauthorized\", \"only admin see sales\"",HttpStatus.UNAUTHORIZED);

        try {
            // Validate month
            int monthInt = Integer.parseInt(month);
            if (monthInt < 1 || monthInt > 12) {
                return ResultUtil.generateResponse("Invalid month. Month must be between 1 (January) and 12 (December).", HttpStatus.BAD_REQUEST);
            }

            // Validate year
            int yearInt = Integer.parseInt(year);
            if (yearInt < 1900 || yearInt > LocalDate.now().getYear()) {
                return ResultUtil.generateResponse("Invalid year. Year must be between 1900 and the current year.", HttpStatus.BAD_REQUEST);
            }

            // Call service method if validation passes
            return saleService.getTotalRevenueForMonthAndYear(monthInt, yearInt);
        } catch (NumberFormatException e) {
            return ResultUtil.generateResponse("Month and year must be valid integers.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/sales/revenue/{saleDate}")
    public ResponseEntity<Object> getTotalRevenueForDayAndMonthAndYear(HttpServletRequest httpServletRequest,@PathVariable String saleDate) {
        String role= jwtUtil.getUsernameFromToken(httpServletRequest.getHeader(AUTHORIZATION).replace(BEARER, ""));
        if (!"admin".equalsIgnoreCase(role))
            return ResultUtil.generateResponse("\"Unauthorized\", \"only admin see sales\"",HttpStatus.UNAUTHORIZED);

        try {
            LocalDate date = LocalDate.parse(saleDate, DATE_FORMATTER);
            return saleService.getTotalRevenueForDay(date);
        } catch (DateTimeParseException e) {
            return ResultUtil.generateResponse("Invalid date format. Please use 'yyyy-MM-dd'.", HttpStatus.BAD_REQUEST);
        }
    }
}


