package com.example.lana.products.service.impl;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.repository.BasketRepository;
import com.example.lana.products.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultBasketService implements BasketService {

    private final BasketRepository basketRepository;

    @Override
    public Basket createBasket() {
        return basketRepository.create();
    }
}
