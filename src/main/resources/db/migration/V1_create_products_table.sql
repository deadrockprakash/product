CREATE TABLE products (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(500),
  price NUMERIC(12,2) NOT NULL,
  quantity INTEGER NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);