package com.example.lana.products.service.impl;

import com.example.lana.products.dto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NoDiscountsServiceTest {

    NoDiscountsService noDiscountsService;

    @BeforeEach
    public void setUp() {
        noDiscountsService = new NoDiscountsService();
    }

    @Test
    @DisplayName("When execute No Discount Service with 3 Products should return total without discount")
    void testNoDiscountServiceShouldReturnTotalSumFor3Products() {
        Double expected = 22.50;
        Product productToApplyDiscount = Product.MUG;
        List<Product> products = List.of(Product.MUG, Product.MUG, Product.MUG);

        Double response = noDiscountsService.getSubtotalWithDiscount(products, productToApplyDiscount);

        assertNotNull(response);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("When execute No Discount Service should return 0.00 by PEN is not in List")
    void test2x1DiscountShouldReturnZeroByNoProductAppears() {
        Double expected = 0.00;
        Product productToApplyDiscount = Product.PEN;
        List<Product> products = List.of(Product.TSHIRT, Product.MUG);

        Double response = noDiscountsService.getSubtotalWithDiscount(products, productToApplyDiscount);

        assertNotNull(response);
        assertEquals(expected, response);
    }

}