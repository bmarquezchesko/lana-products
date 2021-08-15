package com.example.lana.products.integration.controller;

import com.example.lana.products.LanaProductsApplication;
import com.example.lana.products.dto.Basket;
import com.example.lana.products.dto.Product;
import com.example.lana.products.repository.BasketRepository;
import com.example.lana.products.request.AddProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LanaProductsApplication.class)
@AutoConfigureMockMvc
public class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BasketRepository basketRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Successful Requests")
    class SuccessfulRequests {

        @Test
        void testCreateBasketSuccessfullyWithStatus200ReturnsNewBasket() throws Exception {
            Basket expectedBasket = new Basket().setId(1L);

            doReturn(expectedBasket).when(basketRepository).save(any());

            mockMvc.perform(post("/lana-api/baskets")
                            .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(expectedBasket.getId()))
                    .andExpect(jsonPath("$.products").isArray())
                    .andExpect(jsonPath("$.products", hasSize(0)))
            ;
        }

        @Test
        void testAddProductToEmptyBasketReturnsSuccessfullyWithStatus200() throws Exception {
            Basket emptyBasket = new Basket().setId(1L);
            Basket expectedBasket = new Basket().setId(1L).setProducts(List.of(Product.PEN, Product.PEN, Product.PEN));
            AddProductRequest addProductRequest = new AddProductRequest()
                    .setProduct(Product.PEN)
                    .setQuantity(3L);

            doReturn(Optional.of(emptyBasket)).when(basketRepository).findById(any());
            doReturn(expectedBasket).when(basketRepository).save(any());

            mockMvc.perform(patch("/lana-api/baskets/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8")
                            .content(objectMapper.writeValueAsString(addProductRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(expectedBasket.getId()))
                    .andExpect(jsonPath("$.products").isArray())
                    .andExpect(jsonPath("$.products", hasSize(3)))
                    .andExpect(jsonPath("$.products[0]").value(expectedBasket.getProducts().get(0).name()))
                    .andExpect(jsonPath("$.products[1]").value(expectedBasket.getProducts().get(1).name()))
                    .andExpect(jsonPath("$.products[2]").value(expectedBasket.getProducts().get(2).name()))
            ;
        }

        @Test
        void testAddProductToBasketWithOtherProductsReturnsSuccessfullyWithStatus200() throws Exception {
            Basket preexistingBasket = new Basket().setId(1L).setProducts(List.of(Product.MUG, Product.MUG));
            Basket expectedBasket = new Basket().setId(1L).setProducts(List.of(Product.MUG, Product.MUG, Product.TSHIRT));
            AddProductRequest addProductRequest = new AddProductRequest()
                    .setProduct(Product.TSHIRT)
                    .setQuantity(1L);

            doReturn(Optional.of(preexistingBasket)).when(basketRepository).findById(any());
            doReturn(expectedBasket).when(basketRepository).save(any());

            mockMvc.perform(patch("/lana-api/baskets/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8")
                            .content(objectMapper.writeValueAsString(addProductRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(expectedBasket.getId()))
                    .andExpect(jsonPath("$.products").isArray())
                    .andExpect(jsonPath("$.products", hasSize(3)))
                    .andExpect(jsonPath("$.products[0]").value(expectedBasket.getProducts().get(0).name()))
                    .andExpect(jsonPath("$.products[1]").value(expectedBasket.getProducts().get(1).name()))
                    .andExpect(jsonPath("$.products[2]").value(expectedBasket.getProducts().get(2).name()))
            ;
        }

        @Test
        void testGetTotalDetailReturnsSuccessfullyWithStatus200() throws Exception {
            List<Product> expectedProducts = List.of(Product.PEN, Product.TSHIRT, Product.MUG);
            Double expectedTotal = 32.50;

            Basket basket = new Basket().setId(1L).setProducts(expectedProducts);

            doReturn(Optional.of(basket)).when(basketRepository).findById(any());

            mockMvc.perform(get("/lana-api/baskets/{basketId}/total_detail", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.products").isArray())
                    .andExpect(jsonPath("$.products", hasSize(3)))
                    .andExpect(jsonPath("$.products[0]").value(expectedProducts.get(0).name()))
                    .andExpect(jsonPath("$.products[1]").value(expectedProducts.get(1).name()))
                    .andExpect(jsonPath("$.products[2]").value(expectedProducts.get(2).name()))
                    .andExpect(jsonPath("$.total").value(expectedTotal))
            ;
        }

        @Test
        void testGetTotalDetailWithBulkDiscountReturnsSuccessfullyWithStatus200() throws Exception {
            List<Product> expectedProducts = List.of(Product.PEN, Product.TSHIRT, Product.TSHIRT, Product.TSHIRT, Product.TSHIRT);
            Double expectedTotal = 65.00;

            Basket basket = new Basket().setId(1L).setProducts(expectedProducts);

            doReturn(Optional.of(basket)).when(basketRepository).findById(any());

            mockMvc.perform(get("/lana-api/baskets/{basketId}/total_detail", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.products").isArray())
                    .andExpect(jsonPath("$.products", hasSize(5)))
                    .andExpect(jsonPath("$.products[0]").value(expectedProducts.get(0).name()))
                    .andExpect(jsonPath("$.products[1]").value(expectedProducts.get(1).name()))
                    .andExpect(jsonPath("$.products[2]").value(expectedProducts.get(2).name()))
                    .andExpect(jsonPath("$.products[3]").value(expectedProducts.get(3).name()))
                    .andExpect(jsonPath("$.products[4]").value(expectedProducts.get(4).name()))
                    .andExpect(jsonPath("$.total").value(expectedTotal))
            ;
        }

        @Test
        void testGetTotalDetailWith25PercentDiscountReturnsSuccessfullyWithStatus200() throws Exception {
            List<Product> expectedProducts = List.of(Product.PEN, Product.TSHIRT, Product.MUG);
            Double expectedTotal = 32.50;

            Basket basket = new Basket().setId(1L).setProducts(expectedProducts);

            doReturn(Optional.of(basket)).when(basketRepository).findById(any());

            mockMvc.perform(get("/lana-api/baskets/{basketId}/total_detail", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.products").isArray())
                    .andExpect(jsonPath("$.products", hasSize(3)))
                    .andExpect(jsonPath("$.products[0]").value(expectedProducts.get(0).name()))
                    .andExpect(jsonPath("$.products[1]").value(expectedProducts.get(1).name()))
                    .andExpect(jsonPath("$.products[2]").value(expectedProducts.get(2).name()))
                    .andExpect(jsonPath("$.total").value(expectedTotal))
            ;
        }

        @Test
        void testDeleteBasketReturnsSuccessfullyWithStatus200() throws Exception {
            doNothing().when(basketRepository).deleteById(any());

            mockMvc.perform(delete("/lana-api/baskets/{basketId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value("Basket deleted successfully!"))
            ;
        }

        @Test
        void testGetAllBasketsSuccessfullyWithStatus200ReturnsTwoBaskets() throws Exception {
            List<Basket> expectedBaskets = buildBasketResponse();

            doReturn(expectedBaskets).when(basketRepository).findAll();

            mockMvc.perform(get("/lana-api/baskets")
                            .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.baskets").isArray())
                    .andExpect(jsonPath("$.baskets", hasSize(2)))

                    .andExpect(jsonPath("$.baskets[0].id").value(expectedBaskets.get(0).getId()))
                    .andExpect(jsonPath("$.baskets[0].products").isArray())
                    .andExpect(jsonPath("$.baskets[0].products", hasSize(3)))
                    .andExpect(jsonPath("$.baskets[0].products[0]").value(expectedBaskets.get(0).getProducts().get(0).name()))
                    .andExpect(jsonPath("$.baskets[0].products[1]").value(expectedBaskets.get(0).getProducts().get(1).name()))
                    .andExpect(jsonPath("$.baskets[0].products[2]").value(expectedBaskets.get(0).getProducts().get(2).name()))

                    .andExpect(jsonPath("$.baskets[1].id").value(expectedBaskets.get(1).getId()))
                    .andExpect(jsonPath("$.baskets[1].products").isArray())
                    .andExpect(jsonPath("$.baskets[1].products", hasSize(2)))
                    .andExpect(jsonPath("$.baskets[1].products[0]").value(expectedBaskets.get(1).getProducts().get(0).name()))
                    .andExpect(jsonPath("$.baskets[1].products[1]").value(expectedBaskets.get(1).getProducts().get(1).name()))
            ;
        }

        private List<Basket> buildBasketResponse() {
            List<Basket> baskets = new ArrayList();
            baskets.add(new Basket().setId(1L).setProducts(List.of(Product.PEN, Product.TSHIRT, Product.MUG)));
            baskets.add(new Basket().setId(2L).setProducts(List.of(Product.TSHIRT, Product.TSHIRT)));

            return baskets;
        }
    }

    @Nested
    @DisplayName("Unsuccessful Requests")
    class UnsuccessfulRequests {
        @Test
        void testAddProductToNotExistingBasketShouldFailWithStatus404() throws Exception {
            AddProductRequest addProductRequest = new AddProductRequest()
                    .setProduct(Product.PEN)
                    .setQuantity(3L);

            mockMvc.perform(patch("/lana-api/baskets/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8")
                            .content(objectMapper.writeValueAsString(addProductRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Basket Not Found Exception"))
                    .andExpect(jsonPath("$.message").value("The basket with ID 1 does not exist"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            ;
        }

        @Test
        void testAddNotValidProductShouldFailWithStatus400() throws Exception {
            AddProductRequest addProductRequest = new AddProductRequest()
                    .setProduct(null)
                    .setQuantity(2L);

            mockMvc.perform(patch("/lana-api/baskets/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8")
                            .content(objectMapper.writeValueAsString(addProductRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Method Argument Not Valid"))
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0]").value("Please provide a 'product' attribute in JSON request"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            ;
        }

        @Test
        void testAddNotValidQuantityShouldFailWithStatus400() throws Exception {
            AddProductRequest addProductRequest = new AddProductRequest()
                    .setProduct(Product.PEN)
                    .setQuantity(null);

            mockMvc.perform(patch("/lana-api/baskets/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8")
                            .content(objectMapper.writeValueAsString(addProductRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Method Argument Not Valid"))
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0]").value("Please provide a 'quantity' attribute in JSON request"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            ;
        }

        @Test
        void testAddNegativeQuantityShouldFailWithStatus400() throws Exception {
            AddProductRequest addProductRequest = new AddProductRequest()
                    .setProduct(Product.PEN)
                    .setQuantity(-5L);

            mockMvc.perform(patch("/lana-api/baskets/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8")
                            .content(objectMapper.writeValueAsString(addProductRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Method Argument Not Valid"))
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0]").value("quantity should be greater or equal to 1"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            ;
        }

        @Test
        void testAddNotValidProductShouldFailWithStatus422() throws Exception {
            String bodyProductWrong = buildBodyToFail("PEPE", 2L);

            mockMvc.perform(patch("/lana-api/baskets/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8")
                            .content(bodyProductWrong))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.error").value("Deserializing Request Exception"))
                    .andExpect(jsonPath("$.message").value("JSON parse error: Cannot deserialize value of type `com.example.lana.products.dto.Product` from String \"PEPE\": " +
                            "not one of the values accepted for Enum class: [PEN, TSHIRT, MUG]; nested exception is com.fasterxml.jackson.databind.exc.InvalidFormatException: " +
                            "Cannot deserialize value of type `com.example.lana.products.dto.Product` from String \"PEPE\": not one of the values accepted for Enum class: [PEN, TSHIRT, MUG]\n" +
                            " at [Source: (PushbackInputStream); line: 1, column: 15] (through reference chain: com.example.lana.products.request.AddProductRequest[\"product\"])"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
            ;
        }

        @Test
        void testDeleteNotExistingBasketShouldFailWithStatus404() throws Exception {
            Mockito.doThrow(EmptyResultDataAccessException.class).when(basketRepository).deleteById(any());

            mockMvc.perform(delete("/lana-api/baskets/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .characterEncoding("utf-8"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Basket Not Found Exception"))
                    .andExpect(jsonPath("$.message").value("The basket with ID 1 does not exist"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            ;
        }

        private String buildBodyToFail(String product, Long quantity) {
            return String.format("{"
                    + "  \"product\": \"%s\","
                    + "  \"quantity\": %d"
                    + "}", product, quantity);
        }
    }
}