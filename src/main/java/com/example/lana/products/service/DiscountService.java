package com.example.lana.products.service;

import com.example.lana.products.dto.Product;

import java.util.List;

public interface DiscountService {

    Double calculateTotalWithDiscount(List<Product> products, Product productToApplyDiscount);
}
