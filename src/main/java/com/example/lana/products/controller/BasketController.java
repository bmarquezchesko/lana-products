package com.example.lana.products.controller;

import com.example.lana.products.dto.Basket;
import com.example.lana.products.request.AddProductRequest;
import com.example.lana.products.response.TotalAmountResponse;
import com.example.lana.products.service.BasketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/baskets")
@Slf4j
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping
    public ResponseEntity<Basket> createBasket(){
        log.info("### POST request to create a basket - Endpoint /baskets was invoked ###");

        Basket response = basketService.createBasket();

        log.info("### Finish POST request to create a user successfully with response {} ###", response);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PatchMapping("/{basketId}")
    public ResponseEntity<Basket> addProduct(@PathVariable Long basketId,
                                             @RequestBody AddProductRequest request){
        log.info("### PATCH request to add product - Endpoint /baskets/{} was invoked with request {} ###", basketId, request);

        Basket response = basketService.addProduct(basketId, request.getProduct());

        log.info("### Finish PATCH request to add product to basket: {} ### ", response);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/{basketId}/total")
    public ResponseEntity<TotalAmountResponse> getTotalAmount(@PathVariable Long basketId){
        log.info("### GET request to obtain total amount from a basket  - Endpoint /baskets/{}/total ###", basketId);

        TotalAmountResponse response = new TotalAmountResponse(basketService.getTotalAmount(basketId));

        log.info("### Finish GET request to obtain total amount from a basket: {} ### ", response);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Basket>> getAllBaskets(){
        log.info("### GET request to obtain all baskets - Endpoint /basket/all was invoked ###");

        List<Basket> response = basketService.getAll();

        log.info("### Finish GET request to obtain all baskets {}} ###", response);

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
