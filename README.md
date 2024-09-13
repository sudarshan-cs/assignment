# GST Calculation API(refer the document to run the app, Pls connect(+917892134475,sudarshanshetty2000@gmail.com) if your unable to run the app)

## Overview
This project is a Spring Boot application that calculates final price after tax for various product categories.

 ## There are 2 option to run the project
 ### Option1.
#### Prerequisites: JAVA 17 and MYSQL running in default port 3306
- Navigate to the **[Releases](https://github.com/sudarshan-cs/assignment/releases/download/taxApp/assignment-0.0.1-SNAPSHOT.jar)** section of this repository.(clicking on Releases leads to jar download)
- Download the latest JAR file (e.g., `assignment-0.0.1-SNAPSHOT.jar`).
- Open a terminal or command prompt and navigate to the directory where jar file is downloaded
- **Run the Application**:
    - Run the following command to start the application, passing your database username and password:
      ```bash
       java -jar assignment-0.0.1-SNAPSHOT.jar ^
      --spring.datasource.username=root ^
      --spring.datasource.password=Admin@123
      ```

    - Replace `root` and `Admin@123` with your actual database username and password.

### Option2.
#### Prerequisites: JAVA 17,Maven(build tool) and MYSQL running in default port 3306
- git clone https://github.com/sudarshan-cs/assignment
  - cd assignment(Open a terminal or command prompt and navigate to the directory where pom file resides)
- **Run the Application**:
    - Run mvn clean package
    - Run the following command to start the application, passing your database username and password:
      ```bash
      java -jar assignment-0.0.1-SNAPSHOT.jar \
      --spring.datasource.username=root \
      --spring.datasource.password=Admin@123
      ```

    - Replace `root` and `Admin@123` with your actual database username and password.


### Access the Application:(Swagger UI is included http://localhost:8080/swagger-ui/index.html)

- The API will be available at http://localhost:8080.
- Swagger API Documentation: You can access the Swagger UI at http://localhost:8080/swagger-ui/index.html to view and test all available API endpoints.



## Features
### Admin Features:

- Create categories and assign GST Rate
- Create products with price in various categories.
- View daily sales and revenue summaries.

### User Features:
- Record sales for multiple products
- Generate bills with calculated taxes.

## All api requires Authorization header with "Bearer " prefix
### You can use below token in authorization header for api call
### Bearer token for admin : 
- eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTcyNjE2MzUwNSwiZXhwIjoxNzI2NzY4MzA1fQ.l1bCXbVnO2jGo6KAKqsZbJ7Y6ryNLMciD7k11bDVitNzqNzUkIs457koT9X8JzS6
### Bearer token for user :
- eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3MjYxNjM1ODIsImV4cCI6MTcyNjc2ODM4Mn0.Kg6TR4XNkSSmrQIBmcPx4OVQyybR-7ijmm6q1SUD2cV6hN3KTs0dUPDzJ8UEcyKr

### Note:you can also call /api/auth/authenticate to generate token, refer api doc below

## API description
#### 1.Create a New Category (Admin Only)
  - Endpoint: POST /api/admin/categories
  - Description: Creates new categories with gst rate
  - Authorization: Admin role required (pass Admin Bearer Token in authorisation header).
  - Request Body: 
     -  {
    "categories": [
    {
    "categoryName": "Food",
    "gstRate": 5.25
    },
    {
    "categoryName": "Footwear",
    "gstRate": 100
    }
    ]
    }

  - Response: 
      - Returns the success msg after creating categories. If user tries to create existing category, Those categories are discarded and remaining categories are created with discard msg response
      - Returns  http status code 406 if gstRate is not within the bound of type DECIMAL(5, 2)
#### 2. Get All Categories (Accessible to All)
- Endpoint: GET /api/all/categories
- Description: Fetches a list of all categories.
- Authorization: Pass either user token or admin token
- Response: Returns the list of all categories available.
   - {
  "data": [
  {
  "id": 1,
  "categoryName": "Food",
  "gstRate": 5.25
  },
  {
  "id": 2,
  "categoryName": "Footwear",
  "gstRate": 100
  }
  ]
  }
   - if there is no categories than http no content code 204 is returned

#### 3. Add a New Product (Admin Only)
  - Endpoint: POST /api/admin/products
  -  Description: Adds a new product for a specific category.
  - Authorization: Admin role required (pass Admin Bearer Token in authorization header).
  - Request Body:
     - {
    "categoryId": 1,
    "products": [
    {
    "productName": "rice",
    "price": 99999999.99
    },{
    "productName": "milk",
    "price": 26.99
    }
    ]
    }

- Response:
    - Returns the success msg after creating products under category. If user tries to create existing product under a category, Those products are discarded and remaining products are created with discard msg response
    - Returns  http status code 406 if gstRate is not within the bound of type DECIMAL(5, 2)
    - Returns http status code 404 if categoryId does not exist in db


#### 4.Get Products by Category ID (Accessible to All)
  - Endpoint: GET /api/all/product/{categoryId}
  - Description: Fetches all products for a specific category ID.
  - Path Variable: any category id returned in get category api 
  - Response: 
     - Returns the list of products for the specified category.
         - {
    "data": [
    {
    "id": 14,
    "productName": "shoe",
    "price": 1
    },
    {
    "id": 15,
    "productName": "bag",
    "price": 1
    }
    ]
    }
    - If category id is invalid than http status code 404 is returned with msg

#### 5.Register Sales (User Only)
  - Endpoint: POST /api/user/sales
  - Description: Registers a new sale.
  - Authorization: User role required (pass User Bearer Token in authorization header).
  - If the same product id exist multiple times in request body than we merge their quantity and record a sale for that product 
  - Request Body: 
     - {
       "sales": [
       {
       "productId": 1,
       "quantity": 2
       },
       {
       "productId": 2,
       "quantity": 2
       }
       ]
       }
  - Response:
    - Return the TotalBill, Sale response of each product, Invalid Product Ids sent in request 
      - {
       "data": {
       "InvalidProductIds": [],
       "totalBill": 61.1525,
       "saleResponses": [
       {
       "pricePerUnitAfterTax": 20.55125,
       "totalPriceAfterTax": 41.1025,
       "saleDate": "2024-09-13",
       "productName": "q",
       "productPrice": 20.5,
       "categoryName": "a",
       "gstRate": 0.25,
       "quantity": 2
       },
       {
       "pricePerUnitAfterTax": 10.025,
       "totalPriceAfterTax": 20.05,
       "saleDate": "2024-09-13",
       "productName": "r",
       "productPrice": 10,
       "categoryName": "a",
       "gstRate": 0.25,
       "quantity": 2
       }
       ]
       }
       }

#### 6. Get Sales for a Specific Day (Admin Only)
- Endpoint: GET /api/admin/sales/day/{saleDate}
- Description: Fetches all sales made on a specific date.
- Authorization: Admin role required (Admin Bearer Token).
- Path Variable: 2024-09-13
   -saleDate: Date in yyyy-MM-dd format.
- Response: 
   - Returns sales data for the specified date.
     - {
       "data": [
       {
       "product": {
       "id": 1,
       "productName": "q",
       "price": 20.5
       },
       "quantity": 2,
       "totalPricePerUnit": 20.55,
       "saleDate": "2024-09-13"
       },
       {
       "product": {
       "id": 11,
       "productName": "y",
       "price": 1
       },
       "quantity": 2,
       "totalPricePerUnit": 1,
       "saleDate": "2024-09-13"
       }
       ]
       }
    


#### 7. Get Total Revenue for a Specific Year (Admin Only)
- Endpoint: GET /api/admin/sales/revenue/year/{year}
- Description: Calculates the total revenue for a given year.
- Authorization: Admin role required (Bearer Token).
- Path Variable: 2024
   - year: The year (must be a valid integer between 1900 and the current year).
 - Response: 
   -  Returns the total revenue for the specified year.
     -  {
      "data": 145.36
      }


#### 8. Get Total Revenue for a Specific Month and Year (Admin Only)
- Endpoint: GET /api/admin/sales/revenue/month/{month}/year/{year}
- Description: Calculates total revenue for a specific month and year.
- Authorization: Admin role required (Bearer Token).
- Path Variables:
   - month: Month as an integer (between 1 and 12).
   - year: Year as an integer (between 1900 and the current year).
- Response: 
   - Returns the total revenue for the specified month and year.
       -  {
          "data": 145.36
          }


#### 9. Get Total Revenue for a Specific Day (Admin Only)
- Endpoint: GET /api/admin/sales/revenue/{saleDate}
- Description: Calculates total revenue for a specific day.
- Authorization: Admin role required (Bearer Token).
- Path Variable:
- saleDate: Date in yyyy-MM-dd format.
- Response: 
   - Returns the total revenue for the specified day.
     -  {
     "data": 145.36
     }
#### 10. Authenticate User(public api, no authorization header required)
  -   Endpoint: POST /api/auth/authenticate
  -   Description: generates a JWT token based on their role (username in request body should be either "admin" or "user").
  - Authorization: No authorization required for this endpoint.
  -  Request Body: 
       - username in request body should be either "admin" or "user", for any other string error msg is returned
       -  {
          "username": "admin"
          }
       - OR
       -  {
             "username": "user"
          }
  -Response:
       - returns the jwt token
       - token have expiration set to 7 days from the date of creation
       - eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTcyNjE5MzY4NiwiZXhwIjoxNzI2Nzk4NDg2fQ.OeAEWVOWSVVQQNIsIQlis0Fhjjt95G5D_NeaYjLhGkNQo_S6qBulcagPmHt-6Glx
  

## Database Schema(Flyway script is used to create a schema)
### 1. Category Table
- CREATE TABLE Category (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  category_name VARCHAR(100) NOT NULL UNIQUE,
  gst_rate DECIMAL(5, 2) NOT NULL
  );
### 2. Product Table
- CREATE TABLE Product (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_name VARCHAR(100) NOT NULL,
  category_id BIGINT NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  UNIQUE (product_name, category_id),
  FOREIGN KEY (category_id) REFERENCES Category(id)
  );
### 3.Sale Table
- CREATE TABLE Sale (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  total_price_per_unit DECIMAL(11, 2) NOT NULL,
  sale_date DATE NOT NULL,
  FOREIGN KEY (product_id) REFERENCES Product(id)
  );

