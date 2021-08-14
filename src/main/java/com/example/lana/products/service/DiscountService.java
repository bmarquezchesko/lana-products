package com.example.lana.products.service;

import com.example.lana.products.dto.Product;

import java.util.List;

public interface DiscountService {

    Double getSubtotalWithDiscount(List<Product> products, Product productToApplyDiscount);
}
