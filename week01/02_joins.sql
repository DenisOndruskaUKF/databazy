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