CREATE TABLE Product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category_id BIGINT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    UNIQUE (product_name, category_id),
    FOREIGN KEY (category_id) REFERENCES Category(id)
);