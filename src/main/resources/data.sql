-- Insert test user first (parent table)
-- Password: 'testpassword' encoded with BCrypt
INSERT INTO user (id, name, email, password, phone) VALUES 
(1, 'Test Customer', 'test@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIFdCu5xGzLEzTrFEaYHlD/Vf6VjV2', '1234567890');

-- Insert test customer (child table)
INSERT INTO customer (id, cart_id) VALUES 
(1, 1);

-- Insert test products
INSERT INTO product (id, name, price, description, category, available) VALUES 
(1, 'Margherita Pizza', 15.99, 'Classic pizza with tomato sauce, mozzarella, and basil', 'PIZZA', true),
(2, 'Chicken Burger', 12.50, 'Grilled chicken breast with lettuce, tomato, and mayo', 'BURGER', true),
(3, 'Caesar Salad', 9.99, 'Fresh romaine lettuce with Caesar dressing and croutons', 'SALAD', true),
(4, 'Coca Cola', 2.50, 'Classic soft drink', 'BEVERAGE', true),
(5, 'Chocolate Cake', 6.99, 'Rich chocolate cake with chocolate frosting', 'DESSERT', true);

-- Insert test customer table
INSERT INTO customer_table (id, number, qr_code) VALUES 
(1, 1, 'QR001'),
(2, 2, 'QR002'),
(3, 3, 'QR003'),
(4, 4, 'QR004'),
(5, 5, 'QR005');

-- Create a cart for the test customer
INSERT INTO cart (id, customer_id, total_price) VALUES 
(1, 1, 0.00);