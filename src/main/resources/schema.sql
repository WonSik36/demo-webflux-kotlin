CREATE TABLE PRODUCT (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO PRODUCT (name, price) VALUES ('TV', 1000), ('Radio', 500), ('Phone', 200);