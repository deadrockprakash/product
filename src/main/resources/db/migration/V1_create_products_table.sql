CREATE TABLE products (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR2(255) NOT NULL,
  description VARCHAR2(500),
  price NUMERIC(12,2) NOT NULL,
  quantity INTEGER NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);