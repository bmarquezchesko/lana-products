package com.example.lana.products.repository;

import com.example.lana.products.dto.Basket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasketRepository extends CrudRepository<Basket, Long> {

    Basket save(Basket basket);

    List<Basket> findAll();
}
