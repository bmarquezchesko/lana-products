package com.example.lana.products.service;

import com.example.lana.products.dto.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoDiscountsService implements DiscountService {

    @Override
    public Double getSubtotalWithDiscount(List<Product> products, Product productToApplyDiscount) {

        return products.stream()
                .filter(p -> p.equals(productToApplyDiscount))
                .mapToDouble(p -> p.getPrice())
                .sum();
    }
}
