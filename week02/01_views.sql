-- Active: 1790622608367@@127.0.0.1@5432@retail_sales
-- Active: 1790622608367@@127.0.0.1@5432@superstore
--Uloha 1
CREATE VIEW high_value_customers AS
SELECT c.customer_id,
    c.customer_name,
    sum(o.sales) as total_sales
From customers as c INNER JOIN orders as o on c.customer_id=o.customer_id GROUP BY c.customer_id HAVING sum(o.sales) > 2000;
SELECT * from high_value_customers;

--Uloha 2
CREATE View regional_monthly_sales AS
SELECT c.region,
    date_trunc('month',o.order_date),
    sum(o.sales) as total_sales
FROM customers AS c
INNER JOIN orders AS o ON c.customer_id=o.customer_id
GROUP BY date_trunc('month',o.order_date),c.region;
SELECT * FROM regional_monthly_sales;

--Uloha 2 
CREATE VIEW analyst_orders AS
SELECT order_id,
    customer_id,
    product_id,
    sales,
    quantity,
    discount
From orders;

SELECT * from analyst_orders;