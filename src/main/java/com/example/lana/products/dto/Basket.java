package com.example.lana.products.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class Basket {
    private Long id;
    private List<Product> products;
}
