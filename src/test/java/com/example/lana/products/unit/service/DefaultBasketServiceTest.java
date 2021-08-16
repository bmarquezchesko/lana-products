package com.example.lana.products.unit.service;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.dto.Product;
import com.example.lana.products.dto.TotalDetail;
import com.example.lana.products.exception.BasketNotFoundException;
import com.example.lana.products.repository.BasketRepository;
import com.example.lana.products.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static com.example.lana.products.utils.MockUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultBasketServiceTest {

    @InjectMocks
    private DefaultBasketService basketService;

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private BulkDiscountService bulkDiscountService;

    @Mock
    private TwoForOneDiscountService twoForOneDiscountService;

    @Mock
    private NoDiscountsService noDiscountsService;

    @Mock
    private CurrencyFormatterService currencyFormatterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("When create a basket then save it successfully")
    void testCreateBasket() {
        Basket expected = new Basket();

        mockBasketRepositoryInSave(basketRepository, expected);

        Basket response = basketService.createBasket();

        assertNotNull(response);
        assertEquals(expected, response);
        verify(basketRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("When add 3 product to a empty basket then save it successfully")
    void testAddProductToEmptyBasket() {
        Basket expected = new Basket()
                .setId(10L)
                .setProducts(Arrays.asList(Product.PEN,Product.PEN,Product.PEN));

        Product productToAdd = Product.PEN;
        Long quantity = 3L;

        mockBasketRepositoryInFindById(basketRepository, new Basket());
        mockBasketRepositoryInSave(basketRepository, expected);

        Basket response = basketService.addProduct(10L, productToAdd, quantity);

        assertNotNull(response);
        assertEquals(3, response.getProducts().size());
        assertEquals(expected.getProducts(), response.getProducts());
        verify(basketRepository, times(1)).findById(any());
        verify(basketRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("When add 1 product to a basket with other products then save it successfully")
    void testAddProductToBasketWithOtherProducts() {
        Basket preexistingBasket = new Basket()
                .setId(10L)
                .setProducts(Arrays.asList(Product.MUG, Product.PEN, Product.TSHIRT));
        Basket expected = new Basket()
                .setId(10L)
                .setProducts(Arrays.asList(Product.MUG, Product.PEN, Product.TSHIRT,  Product.TSHIRT));

        Product productToAdd = Product.TSHIRT;
        Long quantity = 1L;

        mockBasketRepositoryInFindById(basketRepository, preexistingBasket);
        mockBasketRepositoryInSave(basketRepository, expected);

        Basket response = basketService.addProduct(10L, productToAdd, quantity);

        assertNotNull(response);
        assertEquals(4, response.getProducts().size());
        assertEquals(expected.getProducts(), response.getProducts());
        verify(basketRepository, times(1)).findById(any());
        verify(basketRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("When add product to a non-existent basket then throws BasketNotFoundException")
    void testAddProductInNonExistentBasketThrowsBasketNotFoundException() {
        Product productToAdd = Product.PEN;
        Long quantity = 3L;

        mockBasketRepositoryInFindById(basketRepository, null);

        Throwable ex = assertThrows(BasketNotFoundException.class, () -> basketService.addProduct(10L, productToAdd, quantity));
        assertEquals("The basket with ID 10 does not exist", ex.getMessage());
        verify(basketRepository, times(1)).findById(any());
        verifyNoMoreInteractions(basketRepository);
    }

    @Test
    @DisplayName("When get total detail then should execute all discount services and return total detail")
    void testGetTotalDetailShouldExecuteAllServiceDiscountAndReturnTotalDetail() {
        Basket basket = new Basket()
                .setId(10L)
                .setProducts(Arrays.asList(Product.MUG, Product.PEN, Product.TSHIRT));
        List<Product> expectedProducts = basket.getProducts();

        mockBasketRepositoryInFindById(basketRepository, basket);
        mockRetrieveDiscountService(bulkDiscountService, 20.00);
        mockRetrieveDiscountService(twoForOneDiscountService, 5.00);
        mockRetrieveDiscountService(noDiscountsService, 7.50);
        mockCurrencyFormatterService(currencyFormatterService, "32.50€");

        TotalDetail response = basketService.getTotalDetail(10L);

        assertEquals(expectedProducts, response.getProducts());
        assertEquals("32.50€", response.getTotal());
        verify(bulkDiscountService, times(1)).getSubtotalWithDiscount(any(), any());
        verify(twoForOneDiscountService, times(1)).getSubtotalWithDiscount(any(), any());
        verify(noDiscountsService, times(1)).getSubtotalWithDiscount(any(), any());
    }

    @Test
    @DisplayName("When get total detail then should execute 2x1Discount and noDiscount services and return total detail")
    void testGetTotalDetailShouldExecuteTwoServiceDiscountAndReturnTotalDetail() {
        Basket basket = new Basket()
                .setId(10L)
                .setProducts(Arrays.asList(Product.MUG, Product.PEN, Product.PEN));
        List<Product> expectedProducts = basket.getProducts();

        mockBasketRepositoryInFindById(basketRepository, basket);
        mockRetrieveDiscountService(twoForOneDiscountService, 5.00);
        mockRetrieveDiscountService(noDiscountsService, 7.50);
        mockCurrencyFormatterService(currencyFormatterService, "12.50€");

        TotalDetail response = basketService.getTotalDetail(10L);

        assertEquals(expectedProducts, response.getProducts());
        assertEquals("12.50€", response.getTotal());
        verifyNoInteractions(bulkDiscountService);
        verify(twoForOneDiscountService, times(1)).getSubtotalWithDiscount(any(), any());
        verify(noDiscountsService, times(1)).getSubtotalWithDiscount(any(), any());
    }

    @Test
    @DisplayName("When get total detail then should execute no discount service and return total detail")
    void testGetTotalDetailShouldExecuteOneServiceDiscountAndReturnTotalDetail() {
        Basket basket = new Basket()
                .setId(10L)
                .setProducts(Arrays.asList(Product.MUG, Product.MUG, Product.MUG));
        List<Product> expectedProducts = basket.getProducts();

        mockBasketRepositoryInFindById(basketRepository, basket);
        mockRetrieveDiscountService(noDiscountsService, 22.50);
        mockCurrencyFormatterService(currencyFormatterService, "22.50€");

        TotalDetail response = basketService.getTotalDetail(10L);

        assertEquals(expectedProducts, response.getProducts());
        assertEquals("22.50€", response.getTotal());
        verifyNoInteractions(bulkDiscountService);
        verifyNoInteractions(twoForOneDiscountService);
        verify(noDiscountsService, times(1)).getSubtotalWithDiscount(any(), any());
    }

    @Test
    @DisplayName("When get total detail to a non-existent basket then throws BasketNotFoundException")
    void testGetTotalDetailInNonExistentBasketThrowsBasketNotFoundException() {
        mockBasketRepositoryInFindById(basketRepository, null);

        Throwable ex = assertThrows(BasketNotFoundException.class, () -> basketService.getTotalDetail(10L));

        assertEquals("The basket with ID 10 does not exist", ex.getMessage());
        verify(basketRepository, times(1)).findById(any());
        verifyNoInteractions(bulkDiscountService);
        verifyNoInteractions(twoForOneDiscountService);
        verifyNoInteractions(noDiscountsService);
    }

    @Test
    @DisplayName("When remove basket should execute deleteById")
    void testRemoveBasket() {
        mockBasketRepositoryInDeleteById(basketRepository);

        basketService.removeBasket(10L);

        verify(basketRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("When get all basket should return a list with three baskets")
    void testGetAllBaskets() {
        Basket firstBasket = new Basket().setId(1L).setProducts(Arrays.asList());
        Basket secondBasket = new Basket().setId(2L).setProducts(Arrays.asList(Product.MUG, Product.PEN, Product.TSHIRT));
        Basket thirdBasket = new Basket().setId(3L).setProducts(Arrays.asList(Product.MUG, Product.PEN));
        List<Basket> expectedBaskets = Arrays.asList(firstBasket, secondBasket, thirdBasket);

        mockBasketRepositoryInFindAll(basketRepository, expectedBaskets);

        List<Basket> response = basketService.getAll();

        assertNotNull(response);
        assertEquals(firstBasket, response.get(0));
        assertEquals(secondBasket, response.get(1));
        assertEquals(thirdBasket, response.get(2));
        assertEquals(expectedBaskets, response);
        verify(basketRepository, times(1)).findAll();
    }

}