package com.example.lana.products.controller;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.service.BasketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/basket")
@Slf4j
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping
    public ResponseEntity<Basket> createBasket(){
        log.info("### POST request to create a basket - Endpoint /basket was invoked");

        Basket response = basketService.createBasket();

        log.info(String.format("### Finish POST request to create a user successfully with response %s ###", response));

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
