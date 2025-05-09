-- Products
INSERT INTO product (id, name, stock, price) VALUES (1, 'Product 1', 81, 2708);
INSERT INTO product (id, name, stock, price) VALUES (2, 'Product 2', 44, 1147);
INSERT INTO product (id, name, stock, price) VALUES (3, 'Product 3', 87, 3773);
INSERT INTO product (id, name, stock, price) VALUES (4, 'Product 4', 97, 1813);
INSERT INTO product (id, name, stock, price) VALUES (5, 'Product 5', 62, 5729);
INSERT INTO product (id, name, stock, price) VALUES (6, 'Product 6', 32, 7754);
INSERT INTO product (id, name, stock, price) VALUES (7, 'Product 7', 13, 3483);
INSERT INTO product (id, name, stock, price) VALUES (8, 'Product 8', 59, 3863);
INSERT INTO product (id, name, stock, price) VALUES (9, 'Product 9', 14, 6056);
INSERT INTO product (id, name, stock, price) VALUES (10, 'Product 10', 77, 2868);
INSERT INTO product (id, name, stock, price) VALUES (11, 'Product 11', 61, 4638);
INSERT INTO product (id, name, stock, price) VALUES (12, 'Product 12', 88, 8396);
INSERT INTO product (id, name, stock, price) VALUES (13, 'Product 13', 79, 2160);
INSERT INTO product (id, name, stock, price) VALUES (14, 'Product 14', 88, 5638);
INSERT INTO product (id, name, stock, price) VALUES (15, 'Product 15', 99, 6432);
INSERT INTO product (id, name, stock, price) VALUES (16, 'Product 16', 21, 5777);
INSERT INTO product (id, name, stock, price) VALUES (17, 'Product 17', 34, 3369);
INSERT INTO product (id, name, stock, price) VALUES (18, 'Product 18', 21, 1250);
INSERT INTO product (id, name, stock, price) VALUES (19, 'Product 19', 62, 9412);
INSERT INTO product (id, name, stock, price) VALUES (20, 'Product 20', 96, 8616);
INSERT INTO product (id, name, stock, price) VALUES (21, 'Product 21', 69, 6793);
INSERT INTO product (id, name, stock, price) VALUES (22, 'Product 22', 70, 3537);
INSERT INTO product (id, name, stock, price) VALUES (23, 'Product 23', 64, 3146);
INSERT INTO product (id, name, stock, price) VALUES (24, 'Product 24', 72, 3312);
INSERT INTO product (id, name, stock, price) VALUES (25, 'Product 25', 45, 3015);
INSERT INTO product (id, name, stock, price) VALUES (26, 'Product 26', 99, 6698);
INSERT INTO product (id, name, stock, price) VALUES (27, 'Product 27', 32, 4870);
INSERT INTO product (id, name, stock, price) VALUES (28, 'Product 28', 67, 2501);
INSERT INTO product (id, name, stock, price) VALUES (29, 'Product 29', 11, 9960);
INSERT INTO product (id, name, stock, price) VALUES (30, 'Product 30', 46, 7578);
INSERT INTO product (id, name, stock, price) VALUES (31, 'Product 31', 97, 7108);
INSERT INTO product (id, name, stock, price) VALUES (32, 'Product 32', 74, 8077);
INSERT INTO product (id, name, stock, price) VALUES (33, 'Product 33', 29, 7617);
INSERT INTO product (id, name, stock, price) VALUES (34, 'Product 34', 24, 1196);
INSERT INTO product (id, name, stock, price) VALUES (35, 'Product 35', 28, 4484);
INSERT INTO product (id, name, stock, price) VALUES (36, 'Product 36', 69, 3500);
INSERT INTO product (id, name, stock, price) VALUES (37, 'Product 37', 67, 6803);
INSERT INTO product (id, name, stock, price) VALUES (38, 'Product 38', 97, 8566);
INSERT INTO product (id, name, stock, price) VALUES (39, 'Product 39', 66, 2946);
INSERT INTO product (id, name, stock, price) VALUES (40, 'Product 40', 71, 7953);
INSERT INTO product (id, name, stock, price) VALUES (41, 'Product 41', 32, 2585);
INSERT INTO product (id, name, stock, price) VALUES (42, 'Product 42', 34, 1011);
INSERT INTO product (id, name, stock, price) VALUES (43, 'Product 43', 88, 4754);
INSERT INTO product (id, name, stock, price) VALUES (44, 'Product 44', 56, 2535);
INSERT INTO product (id, name, stock, price) VALUES (45, 'Product 45', 81, 1439);
INSERT INTO product (id, name, stock, price) VALUES (46, 'Product 46', 74, 6151);
INSERT INTO product (id, name, stock, price) VALUES (47, 'Product 47', 79, 3901);
INSERT INTO product (id, name, stock, price) VALUES (48, 'Product 48', 82, 8195);
INSERT INTO product (id, name, stock, price) VALUES (49, 'Product 49', 53, 7401);
INSERT INTO product (id, name, stock, price) VALUES (50, 'Product 50', 72, 1294);

-- Daily Sales Summary
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (1, 30, 25, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (2, 12, 5, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (3, 14, 193, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (4, 24, 446, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (5, 36, 213, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (6, 49, 349, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (7, 25, 312, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (8, 4, 223, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (9, 6, 381, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (10, 36, 99, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (11, 32, 354, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (12, 47, 77, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (13, 36, 321, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (14, 13, 39, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (15, 9, 303, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (16, 33, 233, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (17, 17, 370, CURDATE());
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (18, 34, 17, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (19, 40, 160, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (20, 9, 212, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (21, 2, 326, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (22, 17, 3, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (23, 16, 185, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (24, 4, 62, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (25, 48, 126, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (26, 3, 302, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (27, 14, 439, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (28, 47, 251, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (29, 8, 354, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (30, 31, 351, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (31, 22, 427, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (32, 7, 497, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (33, 48, 433, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (34, 18, 405, DATE_SUB(CURDATE(), INTERVAL 1 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (35, 35, 233, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (36, 14, 488, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (37, 3, 145, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (38, 44, 427, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (39, 32, 416, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (40, 19, 96, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (41, 23, 24, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (42, 29, 240, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (43, 50, 322, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (44, 47, 332, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (45, 3, 371, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (46, 27, 161, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (47, 9, 382, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (48, 18, 422, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (49, 5, 341, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
INSERT INTO stats_daily_product_sales (id, product_id, sales_count, order_date) VALUES (50, 28, 151, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
