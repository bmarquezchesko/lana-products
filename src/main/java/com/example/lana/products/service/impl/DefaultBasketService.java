package com.example.lana.products.service.impl;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.dto.Product;
import com.example.lana.products.dto.TotalDetail;
import com.example.lana.products.exception.BasketNotFoundException;
import com.example.lana.products.repository.BasketRepository;
import com.example.lana.products.service.BasketService;
import com.example.lana.products.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DefaultBasketService implements BasketService {

    private final BasketRepository basketRepository;
    private final BulkDiscountService bulkDiscountService;
    private final TwoForOneDiscountService twoForOneDiscountService;
    private final NoDiscountsService noDiscountsService;

    @Override
    public Basket createBasket() {
        Basket basket = new Basket();

        return basketRepository.save(basket);
    }

    @Override
    public Basket addProduct(Long basketId, Product product, Long quantity) {
        Optional<Basket> basket = basketRepository.findById(basketId);

        if (basket.isEmpty()) {
            throw new BasketNotFoundException(String.format("The basket with ID %d does not exist", basketId));
        }

        IntStream.range(0, quantity.intValue())
                .forEach(x -> basket.get().getProducts().add(product));

        return basketRepository.save(basket.get());
    }

    @Override
    public TotalDetail getTotalDetail(Long basketId) {
        Optional<Basket> basket = basketRepository.findById(basketId);

        if (basket.isEmpty()) {
            throw new BasketNotFoundException(String.format("The basket with ID %d does not exist", basketId));
        }

        List<Product> products = basket.get().getProducts();
        Double totalWithDiscount = Arrays.stream(Product.values())
                .filter(p -> products.contains(p))
                .mapToDouble(p -> mapOfImplementations().get(p).calculateTotalWithDiscount(products, p))
                .sum();

        return new TotalDetail()
                .setProducts(products)
                .setTotal(totalWithDiscount);
    }

    @Override
    public void removeBasket(Long basketId) {
        basketRepository.deleteById(basketId);
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
