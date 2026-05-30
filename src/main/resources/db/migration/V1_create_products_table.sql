CREATE TABLE products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(500),
  price DECIMAL(12,2) NOT NULL,
  quantity INTEGER NOT NULL,
  created_at DATE,
  updated_at DATE
);
