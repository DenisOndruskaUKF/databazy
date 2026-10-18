-- Active: 1790622608367@@127.0.0.1@5432@retail
--Uloha 1
CREATE DATABASE retail;

CREATE TABLE retail_sales (
    invoice_no   VARCHAR(20),
    stock_code   VARCHAR(50),
    description  TEXT,
    quantity     INTEGER,
    invoice_date TIMESTAMP,
    unit_price   NUMERIC(10, 2),
    customer_id  INTEGER,
    country      VARCHAR(100)
);ALTER DATABASE retail SET datestyle TO 'ISO, DMY';


--Uloha 2
SELECT customer_id,
    invoice_no,
    invoice_date,
    quantity,
    unit_price,
    quantity*unit_price AS revenue,
    sum(quantity*unit_price) over(
        PARTITION BY customer_id
        ORDER BY invoice_date
        range BETWEEN Interval '14 days' PRECEDING AND CURRENT ROW
    ) AS revenue_last_14_days
    from retail_sales
    WHERE customer_id IS NOT NULL
    ORDER BY customer_id, invoice_date;

--Uloha 3
SELECT
    stock_code,
    country,
    invoice_date,
    quantityuhh,
    round(AVG(quantity) OVER(
        PARTITION BY stock_code,country
        ORDER BY invoice_date
        range BETWEEN INTERVAL '7 days' PRECEDING AND CURRENT ROW
    ),2) as avg_quantity_7_days 
    from retail_sales;


--Uloha 4
With orders as(
    SELECT DISTINCT customer_id,
        invoice_no,
        invoice_date::date as invoice_date
        from retail_sales
        WHERE customer_id is not null
),
next_orders as(
    SELECT customer_id,
    invoice_no,
    invoice_date,
    lead(invoice_date,1) over(
        PARTITION BY customer_id
        ORDER BY invoice_date
    ) as next_invoice_date
    from orders
)
SELECT customer_id,
    invoice_no,
    invoice_date,
    next_invoice_date,
    next_invoice_date - invoice_date as days_to_next_invoice
    from next_orders
    WHERE next_invoice_date IS NOT NULL
ORDER BY days_to_next_invoice DESC;
--Uloha 5
with price as(
    select stock_code,
    invoice_no,
    invoice_date,
    LAG(unit_price) over(
        PARTITION BY stock_code
        ORDER BY invoice_date
    ) as previous_price,
    unit_price as current_price
    FROM retail_sales
)
SELECT stock_code,
    invoice_no,
    invoice_date,
    previous_price,
    current_price,
current_price - previous_price as price_difference
from price 
WHERE current_price - previous_price is not null and current_price != previous_price
ORDER BY invoice_date asc;

--Uloha 6
WITH total_sales as(
    SELECT customer_id,
    sum(quantity*unit_price) as total_amount
    from retail_sales
    where customer_id is not NULL
    group By customer_id
)
SELECT customer_id,
    total_amount,
    NTILE(4) over(
        ORDER BY total_amount
    ) as sales_quartile

from total_sales
order by customer_id desc;

--Uloha 7
with noa as(
    SELECT country,
    date_trunc('month',invoice_date) as month,
    sum(quantity*unit_price) as total_amount
    from retail_sales
    GROUP BY country,date_trunc('month',invoice_date)
    )
SELECT country,
    month,
    total_amount,
    ntile(4) over(
        PARTITION BY country
        order by total_amount desc
    ) as sales_quartile
    from noa
    ORDER BY country,month;

--Uloha 8
with first_sale as(
    SELECT customer_id,
    invoice_no,
    invoice_date,
    stock_code,
    description,
    quantity,
    unit_price,
    ROW_NUMBER() over(
        PARTITION BY customer_id
        ORDER BY invoice_date ASC
    ) as first_one
    from retail_sales
      WHERE customer_id IS NOT NULL

)

SELECT 
    customer_id,
    invoice_no,
    invoice_date,
    stock_code,
    description,
    quantity,
    unit_price
    FROM first_sale
    where first_one =1
    ORDER BY customer_id;
   

--Uloha 9
WITH ranked_items AS (
    SELECT invoice_no,
           stock_code,
           description,
           quantity,
           unit_price,
           quantity * unit_price AS amount,
           ROW_NUMBER() OVER (
               PARTITION BY invoice_no
               ORDER BY quantity * unit_price DESC
           ) AS row_num
    FROM retail_sales
)
SELECT invoice_no, stock_code, description, quantity, unit_price, amount, row_num
FROM ranked_items
WHERE row_num <= 3
ORDER BY invoice_no, row_num;
--Uloha 10
WITH customer_monthly_sales AS (
    SELECT country,
           DATE_TRUNC('month', invoice_date)::DATE AS month,
           customer_id,
           SUM(quantity * unit_price)              AS total_amount
    FROM retail_sales
    WHERE customer_id IS NOT NULL
    GROUP BY country, DATE_TRUNC('month', invoice_date), customer_id
),
ranked_customers AS (
    SELECT country,
           month,
           customer_id,
           total_amount,
           DENSE_RANK() OVER (
               PARTITION BY country, month
               ORDER BY total_amount DESC
           ) AS sales_rank
    FROM customer_monthly_sales
)
SELECT country, month, customer_id, total_amount, sales_rank
FROM ranked_customers
WHERE sales_rank <= 5
ORDER BY country, month, sales_rank, customer_id;

--Uloha 11
WITH ranked_items AS (
    SELECT invoice_no,
           stock_code,
           description,
           quantity,
           unit_price,
           DENSE_RANK() OVER (
               PARTITION BY invoice_no
               ORDER BY unit_price DESC
           ) AS price_rank
    FROM retail_sales
)
SELECT invoice_no, stock_code, description, quantity, unit_price, price_rank
FROM ranked_items
WHERE price_rank <= 5
ORDER BY invoice_no, price_rank, stock_code;