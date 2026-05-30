ALTER TABLE products
    ADD FULLTEXT INDEX idx_products_search (name, description);
