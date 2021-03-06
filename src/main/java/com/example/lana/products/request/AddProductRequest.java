package com.example.lana.products.request;

import com.example.lana.products.dto.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AddProductRequest {

    @NotNull(message = "Please provide \'product\' attribute in JSON request")
    private Product product;

    @Min(value = 1, message = "quantity should be greater or equal to 1")
    @NotNull(message = "Please provide \'quantity\' attribute in JSON request")
    private Long quantity;
}
