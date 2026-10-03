-- Active: 1790622608367@@127.0.0.1@5432@superstore
CREATE DATABASE superstore;

CREATE Table customers(
    customer_id VARCHAR(20) PRIMARY KEY,
    customer_name VARCHAR(100),
    segment VARCHAR(50),
    country VARCHAR(50),
    region VARCHAR(50)
);


CREATE TABLE products(
    product_id VARCHAR(20) PRIMARY KEY,
    category VARCHAR(50),
    sub_category VARCHAR(50),
    product_name VARCHAR(50)
);

CREATE TABLE orders(
    order_id VARCHAR(20) PRIMARY KEY,
    order_date DATE,
    ship_date DATE,
    sales NUMERIC,
    quantity INT,
    discount NUMERIC,
    profit NUMERIC,
    customer_id VARCHAR(20), FOREIGN KEY(customer_id) REFERENCES customers(customer_id),
    product_id VARCHAR(20), FOREIGN KEY(product_id) REFERENCES products(product_id)
);

--ULOHA 2
SELECT 
    o.order_id,
    c.customer_name,
    o.sales
FROM orders AS o
JOIN customers AS c ON o.customer_id = c.customer_id
WHERE o.sales > 500
ORDER BY o.sales DESC;

--ULOHA 3
Select 
    o.order_id,
    c.customer_name,
    p.category,
    o.sales 
from orders as o
inner join products as p on o.product_id = p.product_id 
inner join customers as c on c.customer_id = o.customer_id;

--ULOHA 4
Select 
    c.region,
    SUM(o.sales) 
from customers as c
inner join orders as o on c.customer_id = o.customer_id group by c.region;

--ULOHA 5
SELECT 
    p.product_name, 
    SUM(o.sales) 
from customers as c
inner join orders as o on c.customer_id = o.customer_id 
inner join products as p on o.product_id = p.product_id GROUP BY p.product_name;

--Uloha 6
SELECT
    c.customer_name,
    o.order_id,
    o.sales
FROM customers AS c
FULL OUTER JOIN orders AS o
    ON c.customer_id = o.customer_id;

--ULOHA 7
SELECT c.region,
       SUM(o.sales) AS total_sales
FROM public.orders AS o
INNER JOIN public.customers AS c
    ON o.customer_id = c.customer_id
GROUP BY c.region;

--ULOHA 8
SELECT c.customer_name,
       COUNT(DISTINCT o.order_id)
FROM public.customers AS c
LEFT JOIN public.orders AS o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;
--ULOHA 9
SELECT p.category,
       AVG(o.discount)
FROM public.orders AS o
INNER JOIN public.products AS p
    ON o.product_id = p.product_id
GROUP BY p.category;

--ULOHA 10
SELECT c.customer_name,
       SUM(o.sales)
FROM public.customers AS c
INNER JOIN public.orders AS o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING SUM(o.sales) > 2000;

--ULOHA 11
SELECT c.region,
       SUM(o.sales),
       AVG(o.discount),
       COUNT(DISTINCT o.order_id)
FROM public.orders AS o
INNER JOIN public.customers AS c
    ON o.customer_id = c.customer_id
GROUP BY c.region;

--ULOHA 12
SELECT c.region,
       COUNT(CASE WHEN o.sales > 1000 THEN 1 END),
       COUNT(CASE WHEN o.sales <= 1000 THEN 1 END)
FROM public.orders AS o
INNER JOIN public.customers AS c
    ON o.customer_id = c.customer_id
GROUP BY c.region;


--ULOHA 13
SELECT c.customer_name,
       SUM(o.sales),
       AVG(o.discount),
       COUNT(DISTINCT o.order_id),
       CASE
           WHEN SUM(o.sales) > 2500 THEN 'VIP'
           ELSE 'REGULAR'
       END
FROM public.customers AS c
INNER JOIN public.orders AS o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY SUM(o.sales) DESC;