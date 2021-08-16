package com.example.lana.products.utils;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.dto.Product;
import com.example.lana.products.repository.BasketRepository;
import com.example.lana.products.service.CurrencyFormatterService;
import com.example.lana.products.service.DiscountService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class MockUtils {

    public static final Long TEST_BASKET_ID = 10L;

    /**
     * Value Mocks
     */

    public static Double mockSubtotal() {
        return 10.00;
    }

    public static String mockTotalWithFormat() {
        return "127.50€";
    }

    /**
     * DTOs Mocks
     */

    public static Basket mockBasket() {
        return new Basket()
                .setId(TEST_BASKET_ID)
                .setProducts(mockProducts());
    }

    public static List<Product> mockProducts() {
        return List.of(Product.PEN, Product.TSHIRT, Product.MUG);
    }

    /**
     * Service Mocks
     */

    public static void mockRetrieveDiscountService(DiscountService service) {
        mockRetrieveDiscountService(service, mockSubtotal());
    }

    public static void mockRetrieveDiscountService(DiscountService service, Double subtotal) {
        when(service.getSubtotalWithDiscount(any(), any())).thenReturn(subtotal);
    }

    public static void mockCurrencyFormatterService(CurrencyFormatterService service) {
        mockCurrencyFormatterService(service, mockTotalWithFormat());
    }

    public static void mockCurrencyFormatterService(CurrencyFormatterService service, String totalFormatted) {
        when(service.parse(any())).thenReturn(totalFormatted);
    }

    /**
     * Repository Mocks
     */

    public static void mockBasketRepositoryInSave(BasketRepository repository) {
        mockBasketRepositoryInSave(repository, mockBasket());
    }

    public static void mockBasketRepositoryInSave(BasketRepository repository, Basket basket) {
        when(repository.save(any())).thenReturn(basket);
    }

    public static void mockBasketRepositoryInFindById(BasketRepository repository) {
        mockBasketRepositoryInFindById(repository, mockBasket());
    }

    public static void mockBasketRepositoryInFindById(BasketRepository repository, Basket basket) {
        when(repository.findById(any())).thenReturn(Optional.ofNullable(basket));
    }

    public static void mockBasketRepositoryInFindAll(BasketRepository repository) {
        mockBasketRepositoryInFindAll(repository, List.of(mockBasket()));
    }

    public static void mockBasketRepositoryInFindAll(BasketRepository repository, List<Basket> baskets) {
        when(repository.findAll()).thenReturn(baskets);
    }

    public static void mockBasketRepositoryInDeleteById(BasketRepository repository) {
        doNothing().when(repository).deleteById(any());
    }
}
