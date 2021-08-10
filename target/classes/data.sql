DROP TABLE IF EXISTS baskets;
DROP TABLE IF EXISTS products;

CREATE TABLE baskets (
                       id INT AUTO_INCREMENT  PRIMARY KEY
);

CREATE TABLE products (
                       id INT AUTO_INCREMENT  PRIMARY KEY,
                       total INTEGER DEFAULT NULL,
                       basket_id INT NOT NULL,
                       FOREIGN KEY(basket_id) REFERENCES baskets(id)
);
