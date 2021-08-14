package com.example.lana.products.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Accessors(chain = true)
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @ElementCollection(targetClass = Product.class, fetch = FetchType.EAGER)
    private List<Product> products = Collections.emptyList();
}
