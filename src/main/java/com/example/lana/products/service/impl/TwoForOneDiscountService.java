package com.example.lana.products.service.impl;

import com.example.lana.products.dto.Product;
import com.example.lana.products.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwoForOneDiscountService implements DiscountService {

    @Override
    public Double calculateTotalWithDiscount(List<Product> products, Product productToApplyDiscount) {
        Long productOccurrences = products.stream()
                .filter(p -> p.equals(productToApplyDiscount))
                .count();

        Long freeUnitsQuantity = productOccurrences / 2;

        return productToApplyDiscount.getPrice()
                * (productOccurrences - freeUnitsQuantity);
    }
}
