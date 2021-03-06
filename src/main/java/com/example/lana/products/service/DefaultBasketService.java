package com.example.lana.products.service;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.dto.Product;
import com.example.lana.products.dto.TotalDetail;
import com.example.lana.products.exception.BasketNotFoundException;
import com.example.lana.products.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultBasketService implements BasketService {

    private final BasketRepository basketRepository;
    private final BulkDiscountService bulkDiscountService;
    private final TwoForOneDiscountService twoForOneDiscountService;
    private final NoDiscountsService noDiscountsService;
    private final CurrencyFormatterService currencyFormatterService;

    @Override
    public Basket createBasket() {
        Basket basket = basketRepository.save(new Basket());
        log.debug("Basket was created successfully: {}", basket);

        return basket;
    }

    @Override
    public Basket addProduct(Long basketId, Product product, Long quantity) {
        Optional<Basket> basketOpt = basketRepository.findById(basketId);

        if (basketOpt.isEmpty()) {
            log.warn("Basket with ID {} does not exist", basketId);
            throw new BasketNotFoundException(String.format("The basket with ID %d does not exist", basketId));
        }

        Basket basket = basketOpt.get();
        basket.setProducts(updateProductList(product, quantity, basket.getProducts()));
        basket = basketRepository.save(basket);
        log.debug("Basket with ID {} was updated successfully: {} ", basketId, basket);

        return basket;
    }

    private List<Product> updateProductList(Product product, Long quantity, List<Product> products) {
        List<Product> productsUpdated = new ArrayList<>(products);
        //Iterate the quantity indicated to add new products
        IntStream.range(0, quantity.intValue())
                .forEach(x -> productsUpdated.add(product));
        return productsUpdated;
    }

    @Override
    public TotalDetail getTotalDetail(Long basketId) {
        Optional<Basket> basket = basketRepository.findById(basketId);

        if (basket.isEmpty()) {
            log.warn("Basket with ID {} does not exist", basketId);
            throw new BasketNotFoundException(String.format("The basket with ID %d does not exist", basketId));
        }

        List<Product> products = basket.get().getProducts();

        //Iterate by products valid, obtain discounted subtotal for each one and sum them
        Double totalWithDiscount = Arrays.stream(Product.values())
                .filter(p -> products.contains(p))
                .mapToDouble(p -> mapOfImplementations().get(p).getSubtotalWithDiscount(products, p))
                .sum();

        TotalDetail totalDetail = new TotalDetail()
                .setProducts(products)
                .setTotal(currencyFormatterService.parse(totalWithDiscount));

        log.debug("Total detail by Basket ID {} was created: {} ", basketId, totalDetail);

        return totalDetail;
    }

    @Override
    public void removeBasket(Long basketId) {
        try {
            basketRepository.deleteById(basketId);
            log.debug("Basket with ID {} was deleted successfully", basketId);
        } catch (EmptyResultDataAccessException ex) {
            log.warn("Basket with ID {} does not exist", basketId);
            throw new BasketNotFoundException(String.format("The basket with ID %d does not exist", basketId));
        }
    }

    @Override
    public List<Basket> getAll() {
        return basketRepository.findAll();
    }

    private Map<Product, DiscountService> mapOfImplementations() {
        return Map.of(
                Product.PEN, twoForOneDiscountService,
                Product.TSHIRT, bulkDiscountService,
                Product.MUG, noDiscountsService
        );
    }
}
