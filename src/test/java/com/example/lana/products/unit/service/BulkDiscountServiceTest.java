package com.example.lana.products.unit.service;

import com.example.lana.products.dto.Product;
import com.example.lana.products.service.BulkDiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BulkDiscountServiceTest {

    BulkDiscountService bulkDiscountService;

    @BeforeEach
    public void setUp() {
        bulkDiscountService = new BulkDiscountService();
    }

    @Test
    @DisplayName("When execute Bulk Discount Service should apply 25% Discount to T-SHIRT")
    void testBulkDiscountServiceShouldApply25PercentDiscountByMore3Units() {
        Double expected = 45.00;
        Product productToApplyDiscount = Product.TSHIRT;
        List<Product> products = List.of(Product.PEN, Product.TSHIRT, Product.TSHIRT, Product.TSHIRT, Product.MUG);

        Double response = bulkDiscountService.getSubtotalWithDiscount(products, productToApplyDiscount);

        assertNotNull(response);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("When execute Bulk Discount Service should not apply 25% Discount to T-SHIRT")
    void testBulkDiscountServiceShouldNotApply25PercentDiscountByLess3Units() {
        Double expected = 40.00;
        Product productToApplyDiscount = Product.TSHIRT;
        List<Product> products = List.of(Product.PEN, Product.TSHIRT, Product.TSHIRT, Product.MUG);

        Double response = bulkDiscountService.getSubtotalWithDiscount(products, productToApplyDiscount);

        assertNotNull(response);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("When execute Bulk Discount Service should return 0.00 by T-SHIRT is not in List")
    void testBulkDiscountServiceShouldReturnZeroByNoProductAppears() {
        Double expected = 0.00;
        Product productToApplyDiscount = Product.TSHIRT;
        List<Product> products = List.of(Product.PEN, Product.MUG);

        Double response = bulkDiscountService.getSubtotalWithDiscount(products, productToApplyDiscount);

        assertNotNull(response);
        assertEquals(expected, response);
    }

}