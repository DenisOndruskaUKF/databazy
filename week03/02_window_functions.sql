-- Active: 1790622608367@@127.0.0.1@5432@superstore
-- Active: 1790622608367@@127.0.0.1@5432@retail_sales
--uloha 1
WITH ranked_orders AS (
    SELECT c.customer_name,
           o.order_id,
           o.order_date,
           ROW_NUMBER() OVER (
               PARTITION BY o.customer_id
               ORDER BY o.order_date DESC
           ) AS rn
    FROM orders o
    JOIN customers c ON c.customer_id = o.customer_id
)
SELECT customer_name, order_id, order_date
FROM ranked_orders
WHERE rn <= 2
ORDER BY customer_name, rn;


--Uloha 2
WITH customer_profit AS (
    SELECT c.region,
           c.customer_id,
           c.customer_name,
           SUM(o.profit) AS total_profit
    FROM orders o
    JOIN customers c ON c.customer_id = o.customer_id
    GROUP BY c.region, c.customer_id, c.customer_name
)
SELECT region,
       customer_name,
       total_profit,
       RANK()       OVER (PARTITION BY region ORDER BY total_profit DESC) AS rank_profit,
       DENSE_RANK() OVER (PARTITION BY region ORDER BY total_profit DESC) AS dense_rank_profit
FROM customer_profit
ORDER BY region, rank_profit;

--Uloha 3
WITH customer_sales AS (
    SELECT c.customer_name,
           SUM(o.sales) AS total_sales
    FROM orders o
    JOIN customers c ON c.customer_id = o.customer_id
    GROUP BY c.customer_id, c.customer_name
)
SELECT customer_name,
       total_sales,
       NTILE(4) OVER (ORDER BY total_sales DESC) AS quartile
FROM customer_sales
ORDER BY total_sales DESC;

--Uloha 4
WITH monthly AS (
    SELECT DATE_TRUNC('month', order_date)::DATE AS month,
           SUM(sales)                            AS monthly_sales
    FROM orders
    GROUP BY DATE_TRUNC('month', order_date)
)
SELECT month,
       monthly_sales,
       LAG(monthly_sales, 1)  OVER (ORDER BY month) AS prev_month_sales,
       LEAD(monthly_sales, 1) OVER (ORDER BY month) AS next_month_sales
FROM monthly
ORDER BY month;

--Uloha 5
WITH product_totals AS (
    SELECT p.category,
           p.product_name,
           SUM(o.sales) AS product_sales
    FROM orders o
    JOIN products p ON p.product_id = o.product_id
    GROUP BY p.category, p.product_name
)
SELECT category,
       product_name,
       product_sales,
       SUM(product_sales) OVER (PARTITION BY category) AS category_total,
       ROUND(
           product_sales * 100.0 / SUM(product_sales) OVER (PARTITION BY category),
           2
       ) AS pct_of_category
FROM product_totals
ORDER BY category, product_name;

--Uloha 6
WITH customer_orders AS (
    SELECT o.customer_id,
           c.customer_name,
           MAX(o.order_date) OVER (PARTITION BY o.customer_id) AS last_order_date,
           MAX(o.order_date) OVER ()                           AS latest_order_date
    FROM orders o
    JOIN customers c ON c.customer_id = o.customer_id
)
SELECT DISTINCT customer_id,
       customer_name,
       last_order_date
FROM customer_orders
WHERE last_order_date < latest_order_date - INTERVAL '90 days'
ORDER BY last_order_date;