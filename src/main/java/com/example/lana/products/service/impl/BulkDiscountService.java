package com.example.lana.products.service.impl;

import com.example.lana.products.dto.Product;
import com.example.lana.products.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulkDiscountService implements DiscountService {

    private static final Double DISCOUNT_PERCENT = 25.0;
    private static final Double TOTAL_PERCENT = 100.0;
    private static final Long MINIMUM_TO_DISCOUNT = 3L;

    @Override
    public Double getSubtotalWithDiscount(List<Product> products, Product productToApplyDiscount) {
        Long productOccurrences = products.stream()
                .filter(p -> p.equals(productToApplyDiscount))
                .count();

        Double totalAmountWithoutDiscount = productToApplyDiscount.getPrice() * productOccurrences;
        Double totalDiscount = DISCOUNT_PERCENT * totalAmountWithoutDiscount / TOTAL_PERCENT;

        return productOccurrences >= MINIMUM_TO_DISCOUNT
                ? totalAmountWithoutDiscount - totalDiscount
                : totalAmountWithoutDiscount;
    }
}
