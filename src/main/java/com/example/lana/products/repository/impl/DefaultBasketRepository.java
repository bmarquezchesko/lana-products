package com.example.lana.products.repository.impl;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.repository.BasketRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultBasketRepository implements BasketRepository {

    @Override
    public Basket create() {
        return new Basket();
    }
}
