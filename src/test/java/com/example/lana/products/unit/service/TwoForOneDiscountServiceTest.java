package com.example.lana.products.unit.service;

import com.example.lana.products.dto.Product;
import com.example.lana.products.service.TwoForOneDiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TwoForOneDiscountServiceTest {

    TwoForOneDiscountService twoForOneDiscountService;

    @BeforeEach
    public void setUp() {
        twoForOneDiscountService = new TwoForOneDiscountService();
    }

    @Test
    @DisplayName("When execute 2x1 Discount Service with 5 Products should apply 100% Discount to 2P")
    void test2x1DiscountWith5ProductShouldApplyDiscount100PercentToTwoProducts() {
        Double expected = 15.00;
        Product productToApplyDiscount = Product.PEN;
        List<Product> products = List.of(Product.PEN, Product.TSHIRT, Product.PEN, Product.TSHIRT, Product.PEN, Product.PEN, Product.PEN);

        Double response = twoForOneDiscountService.getSubtotalWithDiscount(products, productToApplyDiscount);

        assertNotNull(response);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("When execute 2x1 Discount Service with 1 Products should not apply discount")
    void test2x1DiscountWith1ProductShouldNotApplyDiscount() {
        Double expected = 5.00;
        Product productToApplyDiscount = Product.PEN;
        List<Product> products = List.of(Product.PEN, Product.TSHIRT, Product.TSHIRT);

        Double response = twoForOneDiscountService.getSubtotalWithDiscount(products, productToApplyDiscount);

        assertNotNull(response);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("When execute 2x1 Discount Service should return 0.00 by MUG is not in List")
    void test2x1DiscountShouldReturnZeroByNoProductAppears() {
        Double expected = 0.00;
        Product productToApplyDiscount = Product.MUG;
        List<Product> products = List.of(Product.PEN, Product.TSHIRT, Product.TSHIRT);

        Double response = twoForOneDiscountService.getSubtotalWithDiscount(products, productToApplyDiscount);

        assertNotNull(response);
        assertEquals(expected, response);
    }
}