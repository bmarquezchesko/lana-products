package com.example.lana.products.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public enum Product {

    PEN("Lana Pen", 5.00),
    TSHIRT("Lana T-Shirt", 20.00),
    MUG("Lana Coffee Mug", 7.50);

    private final String name;
    private final Double price;

}
