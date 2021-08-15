# Lana API - Spring Boot
![technology Java](https://img.shields.io/badge/technology-java-blue.svg)
![tag jdk11](https://img.shields.io/badge/tag-jdk11-orange.svg)

Lana API expose services to create, read, update and delete Baskets.
In addition, you could add Lana-Product to a basket:

```
Code         | Name              |  Price
-----------------------------------------------
PEN          | Lana Pen          |   5.00€
TSHIRT       | Lana T-Shirt      |  20.00€
MUG          | Lana Coffee Mug   |   7.50€
```

You could access to some specific discounts like:
- **2x1 Promotion in PENs** 
- **25% Discount in T-SHIRT buying 3 or more units**

## Pre-requirements:

    Maven
    Java JDK 11

## Configure your project
First, you must clone project from git-hub, then you should go to project root directory
and execute next command in console `mvn clean install` to build project.
After that, you could execute application with command `mvn spring-boot:run` and enjoy it ;)

The application will execute in port `8080` from `localhost`.

Once the application is running you could see Baskets store in a memory database (**H2**).
To query elements in DB you could open navigator in `http://localhost:8080/h2-console` and login with credentials:
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

However, you could execute requests in console:

### Create a new Basket

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

**IMPORTANT:**
- Fields `"product"` and `"quantity"` are mandatory.
- `"product"` must be `PEN`,`TSHIRT` or `MUG`. 
- `"quantity"`should be greater or equal to 1.

```
curl --request PATCH 'http://localhost:8080/lana-api/baskets/1' \
--header "Content-Type:application/json" \
--data-raw '{
"product": "PEN",
"quantity": 5
}'
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
  "message": "The basket with ID %d does not exist",
  "status": 404
}
```

### GET All Baskets

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

## Questions
* [braianmarquez89@gmail.com](mailto:braianmarquez89@gmail.com)
