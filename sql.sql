CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    order_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    delivery_address TEXT,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders_end (
    id SERIAL PRIMARY KEY,
    order_number VARCHAR(50) ,
    customer_name VARCHAR(100) ,
    order_date TIMESTAMP ,
    status VARCHAR(20) ,
    total_amount DECIMAL(10, 2) ,
    currency VARCHAR(10) ,
    delivery_address TEXT
);

INSERT INTO orders (order_number, customer_name, order_date, status, total_amount, currency, delivery_address)
VALUES
('ORD-1001', 'Alice Dupont', '2026-01-30 10:15:00', 'NEW', 249.99, 'EUR', '12 rue Lafayette, Paris'),
('ORD-1002', 'Jean Martin', '2026-01-30 11:45:00', 'NEW', 89.50, 'EUR', '5 avenue Victor Hugo, Lyon'),
('ORD-1003', 'Sophie Bernard', '2026-01-29 16:20:00', 'SHIPPED', 320.00, 'EUR', '8 rue de la République, Marseille');
