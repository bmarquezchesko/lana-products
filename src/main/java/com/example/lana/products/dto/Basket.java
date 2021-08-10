package com.example.lana.products.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Data
@Entity
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    @ElementCollection(targetClass = Product.class, fetch = FetchType.EAGER)
    private List<Product> products = Collections.emptyList();
}
