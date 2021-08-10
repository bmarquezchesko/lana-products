package com.example.lana.products.service.impl;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.repository.BasketRepository;
import com.example.lana.products.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Basket> getAll() {
        return basketRepository.findAll();
    }

}
