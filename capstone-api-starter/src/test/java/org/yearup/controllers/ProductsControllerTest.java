package org.yearup.controllers;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.yearup.data.ProductDao;
import org.yearup.models.Product;

class ProductsControllerTest
{
    @Mock
    private ProductDao productDao;

    private ProductsController controller;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        controller = new ProductsController(productDao);
    }

    @Test
    void updateProduct_shouldCallUpdateOnDao()
    {
        // arrange
        var product = new Product();
        product.setName("Test");
        product.setPrice(new BigDecimal("9.99"));

        // act
        controller.updateProduct(5, product);

        // assert
        verify(productDao, times(1)).update(5, product);
    }
}
