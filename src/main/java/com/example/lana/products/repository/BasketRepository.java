package com.example.lana.products.repository;

import com.example.lana.products.dto.Basket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends CrudRepository<Basket, Long> {

    Basket save(Basket basket);

    Optional<Basket> findById(Long id);

    List<Basket> findAll();

    void deleteById(Long id);


}
