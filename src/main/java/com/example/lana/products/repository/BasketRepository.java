package com.example.lana.products.repository;

import com.example.lana.products.dto.Basket;
import org.springframework.data.repository.CrudRepository;

public interface BasketRepository extends CrudRepository<Basket, Long> {

    Basket save(Basket basket);
}
