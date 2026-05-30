package com.prakash.product_service.controller;

import com.prakash.product_service.dto.PagedResponse;
import com.prakash.product_service.dto.ProductDto;
import com.prakash.product_service.exception.ProductCustomException;
import com.prakash.product_service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class ProductControllerTest {
    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    private ProductDto productDto;

    @BeforeEach
     void setup(){
        productDto = ProductDto.builder()
                .name("Iphone 14 Pro Max")
                .description("Apple iPhone 14 Pro Max (128GB) - Space Black")
                .price(new BigDecimal("109900.00"))
                .quantity(10)
                .build();
    }
    @Test
    @DisplayName("save product when success")
     void saveProduct_ShouldReturnId_WhenProductIsValid(){
        Long expectedId = 1L;
        //Arrange
        when(productService.saveProduct(any(ProductDto.class))).thenReturn(expectedId);

        //Act
        ResponseEntity<Long> response = productController.saveProduct(productDto);

        //Assert
        assertNotNull(response);
        assertEquals(expectedId, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(productService,times(1)).saveProduct(any(ProductDto.class));

    }
    @Test
    @DisplayName("verify Service call when product is saved")
     void testSaveProduct_VerifyServiceCall(){
        //Arrange
        when(productService.saveProduct(any(ProductDto.class))).thenReturn(1L);

        //Act
        ResponseEntity<Long> response = productController.saveProduct(productDto);

        //Assert
        assertNotNull(response);
        verify(productService,times(1)).saveProduct(any(ProductDto.class));

    }

    @Test
    @DisplayName("get product when success")
     void getProductById_ShouldReturnProductDto_WhenProductExists(){
        //Arrange
        Long productID = 1L;
        when(productService.getProductById(productID)).thenReturn(productDto);

        //Act
        ResponseEntity<ProductDto> response = productController.getProductById(productID);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
        verify(productService,times(1)).getProductById(productID);
    }

    @Test
     void testGetProductById_VerifyServiceCall(){
        Long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(productDto);
        ResponseEntity<ProductDto> response = productController.getProductById(productId);
        assertNotNull(response);
        verify(productService,times(1)).getProductById(productId);
    }

    @Test
    @DisplayName("get product when product not found")
     void getProductById_ShouldThrowException_WhenProductDoesNotExist(){
        //Arrange
        Long productId = 999L;
        String expectedMessage = "Product not found";
        String expectedError = "PRODUCT_NOT_FOUND";
        when(productService.getProductById(productId)).thenThrow(new ProductCustomException(expectedMessage,expectedError));

        //Act and assert
        ProductCustomException exception = assertThrows(ProductCustomException.class,
                () -> productController.getProductById(productId));

        //verify the exception details
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedError, exception.getErrorCode());
        verify(productService,times(1)).getProductById(productId);
    }

    @Test
    @DisplayName("search products when success")
    void searchProducts_ShouldReturnMatchingProducts() {
        PagedResponse<ProductDto> pagedResponse = PagedResponse.<ProductDto>builder()
                .content(List.of(productDto))
                .page(0)
                .size(10)
                .totalElements(1)
                .totalPages(1)
                .last(true)
                .build();
        when(productService.searchProducts("iphone", 0, 10)).thenReturn(pagedResponse);

        ResponseEntity<PagedResponse<ProductDto>> response = productController.searchProducts("iphone", 0, 10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(productDto, response.getBody().getContent().getFirst());
        assertEquals(0, response.getBody().getPage());
        assertEquals(10, response.getBody().getSize());
        verify(productService, times(1)).searchProducts("iphone", 0, 10);
    }

}
