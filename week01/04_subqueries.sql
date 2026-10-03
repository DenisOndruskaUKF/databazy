--ULOHA 1 (4028)
SELECT 
    product_name, 
    total_amount
 from flourmills_sales 
 WHERE total_amount > (SELECT avg(total_amount) from flourmills_sales);

--ULOHA 2 (a)
 SELECT 
    sales_id, 
    sale_date, 
    region, 
    product_category
from flourmills_sales 
WHERE product_category = (
    SELECT 
    product_category 
    FROM flourmills_sales
    GROUP BY product_category
    ORDER BY SUM(total_amount)
    DESC LIMIT 1) 
    ORDER BY sales_id ASC;


--ULOHA 3 (Golden Penny Flour 50kg)
SELECT 
    product_name,
    total_amount, 
    (SELECT AVG(total_amount) FROM flourmills_sales) AS avg_amount
FROM flourmills_sales;

--ULOHA 4 (a)
SELECT 
    product_name, 
    total_amount,
    total_amount / (SELECT SUM(total_amount) FROM flourmills_sales) AS amount_share
from flourmills_sales;

--Uloha 5 (3801432205.37, pomylil som si stlpce:( )
SELECT 
    month,  
    monthly_sales
    FROM (SELECT EXTRACT(MONTH from sale_date) as month, SUM(total_amount) as monthly_sales from flourmills_sales GROUP BY month)
    ORDER BY monthly_sales DESC;

--Uloha 6 (Oil, Pasta, Flour, Noodles)
SELECT 
    category, 
    total_sales 
    from (SELECT product_category as category, sum(total_amount) as total_sales from flourmills_sales GROUP BY category)
    WHERE total_sales > 50000000
    ORDER BY total_sales DESC;

--Uloha 7(100)
SELECT 
    s.product_name,
    s.product_category,
    s.total_amount
    FROM flourmills_sales s
    WHERE s.total_amount > (
        SELECT AVG(f.total_amount)
        FROM flourmills_sales f
        WHERE f.product_category = s.product_category
    );

--Uloha 8 (a)
SELECT
    s.product_name,
    s.region,
    s.total_amount,
    (SELECT min(f.total_amount) as region_min_amount
    FROM flourmills_sales f
    WHERE f.region = s.region)
FROM flourmills_sales s;

--Uloha 9(Pravda)
SELECT 
    s.product_name,
    s.total_amount
FROM flourmills_sales s
WHERE EXISTS(
    Select 1
    FROM flourmills_sales f
    WHERE f.product_name = s.product_name
    HAVING COUNT(DISTINCT extract(MONTH from f.sale_date)) > 1
);



--Uloha 10(a)
SELECT
    s.product_category,
    s.product_name,
    s.total_amount
FROM flourmills_sales s
WHERE EXISTS (
    SELECT f.total_amount 
    from flourmills_sales f
    WHERE f.product_category = s.product_category
    AND f.total_amount > 200000     
          
    );

--Uloha 11(a)
SELECT
    s.product_category
FROM flourmills_sales s
WHERE EXISTS(
    SELECT 1
    FROM flourmills_sales f
    WHERE f.product_category = s.product_category
    GROUP BY f.product_category
    HAVING count(DISTINCT(f.region)) > 3
)GROUP BY s.product_category;

--Uloha 12(Pravda)
SELECT 
    s.region,
    s.total_amount
FROM flourmills_sales s
WHERE EXISTS(
    SELECT 1
    FROM flourmills_sales f
    WHERE f.region = s.region
    AND extract(YEAR FROM f.sale_date) < 2024
);

--Uloha 13(0)
SELECT DISTINCT
    s.product_category
FROM flourmills_sales s
Where NOT EXISTS(
    SELECT 1
    from flourmills_sales f
    WHERE f.product_category = s.product_category AND f.total_amount > 500000
);

--Uloha 14(a)
SELECT 
    s.region
FROM flourmills_sales s
WHERE NOT EXISTS(
    SELECT 1
    FROM flourmills_sales f
    WHERE f.region = f.region
    AND f.product_category = 'Flour'
);