INSERT INTO users (id, name, created_at, modified_at) VALUES (1, 'user1', '2025-04-17 16:03:30', '2025-04-17 16:03:30');
INSERT INTO users (id, name, created_at, modified_at) VALUES (2, 'user2', '2025-04-17 16:03:30', '2025-04-17 16:03:30');
INSERT INTO users (id, name, created_at, modified_at) VALUES (3, 'user3', '2025-04-17 16:03:30', '2025-04-17 16:03:30');
INSERT INTO users (id, name, created_at, modified_at) VALUES (4, 'user4', '2025-04-17 16:03:30', '2025-04-17 16:03:30');
INSERT INTO users (id, name, created_at, modified_at) VALUES (5, 'user5', '2025-04-17 16:03:30', '2025-04-17 16:03:30');
INSERT INTO users (id, name, created_at, modified_at) VALUES (6, 'user6', '2025-04-17 16:03:30', '2025-04-17 16:03:30');
INSERT INTO users (id, name, created_at, modified_at) VALUES (7, 'user7', '2025-04-17 16:03:30', '2025-04-17 16:03:30');
INSERT INTO users (id, name, created_at, modified_at) VALUES (8, 'user8', '2025-04-17 16:03:30', '2025-04-17 16:03:30');
INSERT INTO users (id, name, created_at, modified_at) VALUES (9, 'user9', '2025-04-17 16:03:30', '2025-04-17 16:03:30');
INSERT INTO users (id, name, created_at, modified_at) VALUES (10, 'user10', '2025-04-17 16:03:30', '2025-04-17 16:03:30');
INSERT INTO coupon (id, name, type, discount_type, discount_amount, expiration_month, issue_start_date, issue_end_date, initial_quantity, quantity,
                    created_at, modified_at, version)
VALUES(1, 'coupon1', 'TOTAL', 'FIXED', 5000, 3, NOW(), DATE_ADD(CURDATE(), INTERVAL 3 DAY), 10, 10, NOW(), NOW(), 0);
