package com.example.lana.products.service;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.dto.Product;
import com.example.lana.products.dto.TotalDetail;

import java.util.List;

public interface BasketService {

    Basket createBasket();

    Basket addProduct(Long basketId, Product product);

    TotalDetail getTotalDetail(Long basketId);

    void removeBasket(Long basketId);

    List<Basket> getAll();

}
