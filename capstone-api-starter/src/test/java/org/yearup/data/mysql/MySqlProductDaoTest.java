package org.yearup.data.mysql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.yearup.models.Product;

class MySqlProductDaoTest extends BaseDaoTestClass
{
    private MySqlProductDao dao;

    @BeforeEach
    public void setup()
    {
        dao = new MySqlProductDao(dataSource);
    }

    @Test
    public void getById_shouldReturn_theCorrectProduct()
    {
        // arrange
        int productId = 1;
        Product expected = new Product()
        {{
            setProductId(1);
            setName("Smartphone");
            setPrice(new BigDecimal("499.99"));
            setCategoryId(1);
            setDescription("A powerful and feature-rich smartphone for all your communication needs.");
            setSubCategory("Black");
            setStock(50);
            setFeatured(false);
            setImageUrl("smartphone.jpg");
        }};

        // act
        var actual = dao.getById(productId);

        // assert
        assertEquals(expected.getPrice(), actual.getPrice(), "Because I tried to get product 1 from the database.");
    }

    @Test
    @Disabled("Requires test database")
    public void search_withMinPrice_shouldReturnProductsAtOrAboveMin()
    {
        // arrange
        var min = new BigDecimal("100");

        // act
        var results = dao.search(null, min, null, null);

        // assert
        assertTrue(results.size() > 0);
        for (Product p : results)
        {
            assertTrue(p.getPrice().compareTo(min) >= 0, "Product price should be >= min");
        }
    }

    @Test
    @Disabled("Requires test database")
    public void search_withMaxPrice_shouldReturnProductsAtOrBelowMax()
    {
        // arrange
        var max = new BigDecimal("200");

        // act
        var results = dao.search(null, null, max, null);

        // assert
        assertTrue(results.size() > 0);
        for (Product p : results)
        {
            assertTrue(p.getPrice().compareTo(max) <= 0, "Product price should be <= max");
        }
    }

    @Test
    @Disabled("Requires test database")
    public void search_withMinAndMaxPrice_shouldReturnProductsInRange()
    {
        // arrange
        var min = new BigDecimal("50");
        var max = new BigDecimal("200");

        // act
        var results = dao.search(null, min, max, null);

        // assert
        assertTrue(results.size() > 0);
        for (Product p : results)
        {
            assertTrue(p.getPrice().compareTo(min) >= 0 && p.getPrice().compareTo(max) <= 0, "Product price should be in range");
        }
    }

    @Test
    @Disabled("Requires test database")
    public void search_withSubCategory_shouldReturnMatchingSubCategory()
    {
        // arrange
        var sub = "Black";

        // act
        var results = dao.search(null, null, null, sub);

        // assert
        assertTrue(results.size() > 0);
        for (Product p : results)
        {
            assertTrue(sub.equals(p.getSubCategory()), "Product subcategory should match");
        }
    }

}