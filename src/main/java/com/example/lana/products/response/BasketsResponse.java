package com.example.lana.products.response;

import com.example.lana.products.dto.Basket;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class BasketsResponse {
    List<Basket> baskets = Collections.emptyList();
}
