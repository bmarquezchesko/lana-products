package com.example.lana.products.dto;

import lombok.Getter;
import javax.persistence.Table;

@Getter
@Table(name = "products")
public enum Product {

    PEN("Lana Pen", 5.00),
    TSHIRT("Lana T-Shirt", 20.00),
    MUG("Lana Coffee Mug", 7.50);

    private final String name;
    private final Double price;

    Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}
