--Uloha 4
CREATE INDEX idx_orders_customer_id 
ON orders(customer_id);

SELECT *
FROM orders
WHERE customer_id = 'C001';

--Uloha 5
CREATE INDEX idx_orders_order_date
ON orders(order_date);

SELECT date_trunc('month',order_date) as month,
    SUM(sales) as sum
FROM orders GROUP BY date_trunc('month',order_date) ORDER BY sum(sales) ASC;

--Uloha 6
CREATE INDEX idx_orders_region_category
ON orders(customer_id,order_date);

CREATE INDEX idx_orders_region_category
ON orders (customer_id, order_date);

SELECT *
FROM orders AS o
INNER JOIN customers AS c
    ON o.customer_id = c.customer_id
WHERE c.region = 'West'
  AND o.order_date >= '2024-01-01';

--Uloha 7
EXPLAIN ANALYZE
SELECT *
FROM orders
WHERE customer_id = 'C001';
