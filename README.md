# Lana API - Spring Boot
![technology Java](https://img.shields.io/badge/technology-java-blue.svg)
![tag jdk11](https://img.shields.io/badge/tag-jdk11-orange.svg)

Lana API expose services to buy awesome Lana merchandising from a physical store 
that sells the following 3 products:

```
Code         | Name              |  Price
-----------------------------------------------
PEN          | Lana Pen          |   5.00€
TSHIRT       | Lana T-Shirt      |  20.00€
MUG          | Lana Coffee Mug   |   7.50€
```

With this fantastic API you will be able to do the following operations:
- Create a new checkout basket
- Add a product to a basket
- Get the total amount in a basket
- Remove the basket
- Get all baskets already created.

You could access to some specific discounts like:
- **2x1 Promotion in PENs** 
- **25% Discount in T-SHIRT buying 3 or more units**

## Pre-requirements:

    Maven
    Java JDK 11

## Configure your project
First, you must clone project from git-hub, then you should go to project root directory
and execute next command in console to build project `mvn clean install`.

* **Running in Local Environment**:
You could execute application in local environment with command `mvn spring-boot:run` and enjoy it :smile: .
The application will execute in port `8080` from `localhost`.


* **Running in Docker Environment**:
On the other hand, you can choose to run application in docker environment with command 
`sudo docker build --tag=lana-products:1.0 .` and then execute `sudo docker run -d -p 8090:8080 -t lana-products:1.0`.
The application will execute in port `8090` from `localhost`.

Once the application is running you could see Baskets stored in a memory database (**H2**).
To query elements in DB you can open navigator in `http://localhost:8080/h2-console` (In docker: port is `8090`) and login with credentials:
```
User Name: sa 
Password: password
```

**IMPORTANT:** If you stop the application, the stored items will be lost because is a database that execute in memory (**H2**).

## Database Diagram

![Database Schema](https://github.com/bmarquezchesko/lana-products/blob/main/src/main/resources/files/DB%20Lana%20Products.png)

## API Usage

All the endpoints and request examples are in this public postman collection:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/94a6d0085384dfadbc93)

####However, you could execute requests in console:

### Create a new Basket

You are allowed to create a new basket executing next request:

```
curl --request POST 'http://localhost:8080/lana-api/baskets'
```

##### Response
**Status**: `200 OK`
```json
{
    "id": 1,
    "products": []
}
```

### Add product to Basket

Also, it is possible to incorporate Products to a pre-existing basket indicating quantity that you want to add, only executing next request:

**IMPORTANT:**
- Fields `"product"` and `"quantity"` are mandatory.
- `"product"` must be `PEN`,`TSHIRT` or `MUG`. 
- `"quantity"`should be greater or equal to 1.

```
curl --request PATCH 'http://localhost:8080/lana-api/baskets/1' \
--header "Content-Type:application/json" \
--data-raw '{ "product": "PEN", "quantity": 5 }'
```

#### Success Response
**Status**: `200 OK`
```json
{
  "id": 1,
  "products": [
    "PEN",
    "PEN",
    "PEN",
    "PEN",
    "PEN"
  ]
}
```
#### Error Response Format
**Status**: `400 Bad Request`
```json
{
  "message": "Method Argument Not Valid",
  "errors": [
    "Please provide 'product' attribute in JSON request",
    "quantity should be greater or equal to 1"
  ],
  "status": 400
}
```
### GET Total Detail by Basket ID
This request will allow you to visualize products chosen in a Basket and its total amount with discounts applied.
```
curl --request GET 'http://localhost:8080/lana-api/baskets/1/total_detail'
```
#### Success Response
**Status**: `200 OK`
```json
{
  "products": [
    "PEN",
    "TSHIRT",
    "PEN",
    "PEN",
    "MUG",
    "TSHIRT",
    "TSHIRT"
  ],
  "total": 62.5
}
```

#### Error Response Format
**Status**: `404 Not Found`
```json
{
  "error": "Basket Not Found Exception",
  "message": "The basket with ID 7 does not exist",
  "status": 404
}
```

### DELETE a Basket by ID
However, if you aren't happy with your basket, you could delete it with the following request:
```
curl --request DELETE 'http://localhost:8080/lana-api/baskets/1'
```

#### Success Response
**Status**: `200 OK`
```
Basket deleted successfully!
```

#### Error Response Format

**Status**: `404 Not Found`
```json
{
  "error": "Basket Not Found Exception",
  "message": "The basket with ID 7 does not exist",
  "status": 404
}
```

### GET All Baskets
Finally, you could request all basket already created before.
```
curl --request GET 'http://localhost:8080/lana-api/baskets'
```

#### Success Response
**Status**: `200 OK`
```json
{
    "baskets": [
        {
            "id": 1,
            "products": [
                "PEN",
                "TSHIRT",
                "MUG"
            ]
        },
        {
            "id": 2,
            "products": [
              "PEN",
              "TSHIRT",
              "PEN",
              "PEN",
              "MUG",
              "TSHIRT",
              "TSHIRT"
            ]
        }
    ]
}
```

## Integration and Unit Test
You be able to run integration and unit tests executing command `mvn clean test`.

## Questions
* [braianmarquez89@gmail.com](mailto:braianmarquez89@gmail.com)
