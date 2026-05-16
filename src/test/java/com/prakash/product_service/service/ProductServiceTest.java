package com.prakash.product_service.service;

import com.prakash.product_service.dto.ProductDto;
import com.prakash.product_service.entity.Product;
import com.prakash.product_service.exception.ProductCustomException;
import com.prakash.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productServiceImpl;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setup(){
        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("Laptop description")
                .price(new BigDecimal("50000.00"))
                .quantity(4)
                .build();

        productDto = ProductDto.builder()
                .name("Laptop")
                .description("Laptop description")
                .price(new BigDecimal("50000.00"))
                .quantity(4)
                .build();

    }

   @Test
   void testSaveProduct_Success() {
       //Arrange
       when(productRepository.save(any(Product.class))).thenReturn(product);

       //act
       Long result = productServiceImpl.saveProduct(productDto);

       //assert
       assertNotNull(result);
       assertEquals(1L, result);
       verify(productRepository).save(any(Product.class));
   }

   @Test
   void testGetProductById_Success() {
       //Arrange
       when(productRepository.findById(1L)).thenReturn(Optional.of(product));

       //act
       ProductDto result = productServiceImpl.getProductById(1L);

       //assert
       assertNotNull(result);
       assertEquals(1L, product.getId());
       assertEquals("Laptop", result.getName());
       assertEquals("Laptop description", result.getDescription());
       assertEquals(new BigDecimal("50000.00"), result.getPrice());
       assertEquals(4, result.getQuantity());
       verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductById_ProductNotFound(){
        Long id = 11111L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());
       assertThrows(ProductCustomException.class,()->productServiceImpl.getProductById(id));
       verify(productRepository).findById(id);

    }




}
