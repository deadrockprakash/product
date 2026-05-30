package com.prakash.product_service.service;

import com.prakash.product_service.dto.PagedResponse;
import com.prakash.product_service.dto.ProductDto;
import com.prakash.product_service.entity.Product;
import com.prakash.product_service.exception.ProductCustomException;
import com.prakash.product_service.messaging.ProductEventPublisher;
import com.prakash.product_service.repository.ProductRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @Mock
    ProductEventPublisher productEventPublisher;

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
       verify(productRepository).save(argThat(savedProduct ->
               "Laptop".equals(savedProduct.getName())
                       && "Laptop description".equals(savedProduct.getDescription())
                       && new BigDecimal("50000.00").equals(savedProduct.getPrice())
                       && Integer.valueOf(4).equals(savedProduct.getQuantity())
       ));
       verify(productEventPublisher).publishProductCreated(argThat(event ->
               event.eventId() != null
                       && !event.eventId().isBlank()
                       && Long.valueOf(1L).equals(event.productId())
                       && "Laptop".equals(event.name())
                       && "Laptop description".equals(event.description())
                       && new BigDecimal("50000.00").equals(event.price())
                       && Integer.valueOf(4).equals(event.quantity())
                       && event.createdAt() != null
       ));
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

    @Test
    void testSearchProducts_WithKeyword() {
        PageRequest pageable = PageRequest.of(0, 10);
        when(productRepository.searchByKeyword("Laptop", pageable))
                .thenReturn(new PageImpl<>(List.of(product), pageable, 1));

        PagedResponse<ProductDto> result = productServiceImpl.searchProducts(" Laptop ", 0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("Laptop", result.getContent().getFirst().getName());
        assertEquals("Laptop description", result.getContent().getFirst().getDescription());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());
        verify(productRepository).searchByKeyword("Laptop", pageable);
        verify(productRepository, never()).findAll();
    }

    @Test
    void testSearchProducts_WithoutKeywordReturnsAllProducts() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(product), pageable, 1));

        PagedResponse<ProductDto> result = productServiceImpl.searchProducts(" ", 0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("Laptop", result.getContent().getFirst().getName());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
        verify(productRepository).findAll(pageable);
        verify(productRepository, never()).searchByKeyword(anyString(), any());
    }



}
