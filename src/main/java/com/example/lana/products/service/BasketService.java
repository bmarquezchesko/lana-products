package com.example.lana.products.service;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.dto.Product;

import java.util.List;

public interface BasketService {

    Basket createBasket();

    Basket addProduct(Long basketId, Product product);

    Double getTotalAmount(Long basketId);

    List<Basket> getAll();

}
