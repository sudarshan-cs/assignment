package com.example.assignment.services.impl;

import com.example.assignment.dto.request.SaleRequest;
import com.example.assignment.dto.response.SaleResponse;
import com.example.assignment.entity.Product;
import com.example.assignment.entity.Sale;
import com.example.assignment.repositories.CategoryRepository;
import com.example.assignment.repositories.ProductRepository;
import com.example.assignment.repositories.SaleRepository;
import com.example.assignment.services.SaleService;
import com.example.assignment.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SaleRepository saleRepository;


    public SaleResponse registerSale(SaleRequest.SaleDetail saleDetail) {
        Optional<Product> product = productRepository.findById(saleDetail.getProductId());
        if (product.isEmpty())
            return null;

        Sale sale=new Sale();
        sale.setProduct(product.get());
        sale.setSaleDate(LocalDate.now());
        sale.setQuantity(saleDetail.getQuantity());
        BigDecimal totalPricePerUnit =getPricePerUnit(product.get());
        sale.setTotalPricePerUnit(totalPricePerUnit);
        Sale savedSale = saleRepository.save(sale);
        return generateSaleResponse(savedSale);

    }

    @Override
    public ResponseEntity<Object> registerSales(SaleRequest saleRequest) {
       saleRequest.mergeDuplicateProductQuantities();
       BigDecimal totalBill=BigDecimal.ZERO;
       List<Long> productIdList=new ArrayList<>();
       List<SaleResponse> saleResponseList=new ArrayList<>();
       for (SaleRequest.SaleDetail saleDetail:saleRequest.getSales())
       {
           SaleResponse saleResponse=registerSale(saleDetail);
           if (saleResponse==null)
               productIdList.add(saleDetail.getProductId());
           else
           {
               saleResponseList.add(saleResponse);
               totalBill = totalBill.add(saleResponse.getTotalPriceAfterTax());
           }
       }
        Map<String, Object> response = new HashMap<>();
        response.put("saleResponses", saleResponseList);
        response.put("InvalidProductIds", productIdList);
        response.put("totalBill", totalBill);
        return ResultUtil.generateResponse(response,HttpStatus.OK);


    }

    @Override
    public ResponseEntity<Object> getSalesForDay(LocalDate saleDate) {
        List<Sale> saleList=saleRepository.findBySaleDate(saleDate);
        if (saleList.isEmpty())
            return ResultUtil.generateResponse(null,HttpStatus.NO_CONTENT);
        return ResultUtil.generateResponse(saleList,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getTotalRevenueForYear(int year) {
        BigDecimal revenue=saleRepository.findTotalRevenueByYear(year);
        if (revenue==null)
            return ResultUtil.generateResponse(null,HttpStatus.NO_CONTENT);
        return ResultUtil.generateResponse(revenue,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getTotalRevenueForMonthAndYear(int month, int year) {
        BigDecimal revenue=saleRepository.findTotalRevenueByMonthAndYear(month,year);
        if (revenue==null)
            return ResultUtil.generateResponse(null,HttpStatus.NO_CONTENT);
        return ResultUtil.generateResponse( revenue,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getTotalRevenueForDay(LocalDate saleDate) {
        BigDecimal revenue=saleRepository.findTotalRevenueByDate(saleDate);
        if (revenue==null)
            return ResultUtil.generateResponse(null,HttpStatus.NO_CONTENT);
        return ResultUtil.generateResponse( revenue,HttpStatus.OK);
    }

    public BigDecimal getPricePerUnit(Product product)
    {
        BigDecimal basePrice = product.getPrice();
        BigDecimal gstRate = product.getCategory().getGstRate();
        BigDecimal gstAmount = basePrice.multiply(gstRate).divide(new BigDecimal(100));
        return basePrice.add(gstAmount);
    }

    public SaleResponse generateSaleResponse(Sale sale)
    {

        SaleResponse saleResponse=new SaleResponse();
        BigDecimal totalPriceAfterTax = sale.getTotalPricePerUnit().multiply(new BigDecimal(sale.getQuantity()));
        saleResponse.setSaleDate(sale.getSaleDate());
        saleResponse.setPricePerUnitAfterTax(sale.getTotalPricePerUnit());
        saleResponse.setTotalPriceAfterTax(totalPriceAfterTax);
        saleResponse.setProductName(sale.getProduct().getProductName());
        saleResponse.setQuantity(sale.getQuantity());
        saleResponse.setProductPrice(sale.getProduct().getPrice());
        saleResponse.setCategoryName(sale.getProduct().getCategory().getCategoryName());
        saleResponse.setGstRate(sale.getProduct().getCategory().getGstRate());
        return saleResponse;
    }
}
