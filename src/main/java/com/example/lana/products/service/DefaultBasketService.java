package com.example.lana.products.service;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.dto.Product;
import com.example.lana.products.dto.TotalDetail;
import com.example.lana.products.exception.BasketNotFoundException;
import com.example.lana.products.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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
        Optional<Basket> basketOpt = basketRepository.findById(basketId);

        if (basketOpt.isEmpty()) {
            throw new BasketNotFoundException(String.format("The basket with ID %d does not exist", basketId));
        }

        Basket basket = basketOpt.get();
        basket.setProducts(updateProductList(product, quantity, basket.getProducts()));

        return basketRepository.save(basket);
    }

    private List<Product> updateProductList(Product product, Long quantity, List<Product> products) {
        List<Product> productsUpdated = new ArrayList<>(products);
        IntStream.range(0, quantity.intValue())
                .forEach(x -> productsUpdated.add(product));
        return productsUpdated;
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
                .mapToDouble(p -> mapOfImplementations().get(p).getSubtotalWithDiscount(products, p))
                .sum();

        return new TotalDetail()
                .setProducts(products)
                .setTotal(totalWithDiscount);
    }

    @Override
    public void removeBasket(Long basketId) {
        try {
            basketRepository.deleteById(basketId);
        } catch (EmptyResultDataAccessException ex) {
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
