package com.example.lana.products.service.impl;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.dto.Product;
import com.example.lana.products.repository.BasketRepository;
import com.example.lana.products.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultBasketService implements BasketService {

    private final BasketRepository basketRepository;

    @Override
    public Basket createBasket() {
        Basket basket = new Basket();

        return basketRepository.save(basket);
    }

    @Override
    public Basket addProduct(Long basketId, Product product) {
        Optional<Basket> basket = basketRepository.findById(basketId);

        if(basket.isPresent()){
            basket.get().getProducts().add(product);

            basketRepository.save(basket.get());
        }

        return basket.get();
    }

    @Override
    public Double getTotalAmount(Long basketId) {
        Optional<Basket> basket = basketRepository.findById(basketId);

        Double totalAmount = basket.isPresent()
                ? basket.get().getProducts().stream().mapToDouble(product -> product.getPrice()).sum()
                : 0.0;

        return totalAmount;
    }

    @Override
    public void removeBasket(Long basketId) {
        basketRepository.deleteById(basketId);
    }

    @Override
    public List<Basket> getAll() {
        return basketRepository.findAll();
    }

}
