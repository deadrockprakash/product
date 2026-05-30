package com.prakash.product_service.repository;

import com.prakash.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(
            value = """
            SELECT *
            FROM products
            WHERE MATCH(name, description) AGAINST (:keyword IN NATURAL LANGUAGE MODE)
            ORDER BY id DESC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM products
            WHERE MATCH(name, description) AGAINST (:keyword IN NATURAL LANGUAGE MODE)
            """, 
            nativeQuery = true
    )
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
