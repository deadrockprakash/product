package com.prakash.product_service.repository;

import com.prakash.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = """
            SELECT *
            FROM products
            WHERE MATCH(name, description) AGAINST (:keyword IN NATURAL LANGUAGE MODE)
            """, nativeQuery = true)
    List<Product> searchByKeyword(@Param("keyword") String keyword);
}
