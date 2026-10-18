-- Active: 1790622608367@@127.0.0.1@5432@superstore
-- Active: 1790622608367@@127.0.0.1@5432@retail_sales
CREATE DATABASE retail_sales;

CREATE TABLE orders (
    order_id    VARCHAR(20) PRIMARY KEY,
    customer_id VARCHAR(20) NOT NULL,
    product_id  VARCHAR(20) NOT NULL,
    order_date  DATE        NOT NULL,
    region      VARCHAR(20) NOT NULL,
    category    VARCHAR(50) NOT NULL,
    ship_mode   VARCHAR(30) NOT NULL,
    sales       NUMERIC(12, 2) NOT NULL,
    profit      NUMERIC(12, 2) NOT NULL
);

ALTER DATABASE retail_sales SET datestyle TO 'ISO, MDY';

SELECT * FROM orders;

--Uloha 8
CREATE Procedure get_customer_sales(
    p_customer_id VARCHAR(20)
)
LANGUAGE plpgsql
AS $procedure$
DECLARE
    v_total_sales NUMERIC(10,2);
BEGIN
Select sum(sales) Into v_total_sales
FROM orders
WHERE p_customer_id = customer_id;
RAISE NOTICE 'Customer: %, total sales: %', p_customer_id, v_total_sales;
END;
$procedure$;
DROP PROCEDURE IF EXISTS get_customer_sales(VARCHAR);

CALL get_customer_sales('C001');


--ULOHA 9
CREATE PROCEDURE apply_regional_discount(
    region_name   VARCHAR(20),
    discount_rate NUMERIC
)
LANGUAGE plpgsql
AS $procedure$
BEGIN
    UPDATE orders o
    SET sales = o.sales * (1 - discount_rate)
    FROM customers c
    WHERE c.customer_id = o.customer_id
      AND c.region = region_name;

    RAISE NOTICE 'Discount: %, region: %', discount_rate, region_name;
END;
$procedure$;

CALL apply_regional_discount('West', 0.10);

--uloha 10
DROP PROCEDURE IF EXISTS get_sales_between(DATE, DATE);

CREATE PROCEDURE get_sales_between(
    start_date DATE,
    end_date   DATE
)
LANGUAGE plpgsql
AS $procedure$
DECLARE
    v_total_sales NUMERIC;
BEGIN
    SELECT SUM(sales)
    INTO v_total_sales
    FROM orders
    WHERE order_date BETWEEN start_date AND end_date;

    RAISE NOTICE 'Start: %, end: %, total sales: %', start_date, end_date, v_total_sales;
END;
$procedure$;

CALL get_sales_between('2024-01-01', '2024-03-31');