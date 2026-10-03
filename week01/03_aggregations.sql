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