package com.example.lana.products.request;

import com.example.lana.products.dto.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddProductRequest {
    private Product product;
}
