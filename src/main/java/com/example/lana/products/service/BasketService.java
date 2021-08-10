package com.example.lana.products.service;

import com.example.lana.products.dto.Basket;

import java.util.List;

public interface BasketService {

    Basket createBasket();

    List<Basket> getAll();
}
