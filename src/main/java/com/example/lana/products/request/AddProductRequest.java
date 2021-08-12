package com.example.lana.products.request;

import com.example.lana.products.dto.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AddProductRequest {

    @NotNull(message = "Please provide a \'product\' attribute in JSON request")
    private Product product;

    @Min(value = 1, message = "quantity should be greater or equal to 1")
    @NotNull(message = "Please provide a \'quantity\' attribute in JSON request")
    private Long quantity;
}
