package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController
{
    private final CategoryDao categoryDao;
    private final ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao)
    {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // =========================
    // GET ALL CATEGORIES
    // =========================
    @GetMapping
    public List<Category> getAll()
    {
        return categoryDao.getAllCategories();
    }

    // =========================
    // GET CATEGORY BY ID
    // =========================
    @GetMapping("/{id}")
    public Category getById(@PathVariable int id)
    {
        return categoryDao.getById(id);
    }

    // =========================
    // GET PRODUCTS BY CATEGORY
    // =========================
    @GetMapping("/{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
      return productDao.listByCategoryId(categoryId);
    }

    // =========================
    // CREATE CATEGORY (ADMIN)
    // =========================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category addCategory(@Valid @RequestBody Category category)
    {
        return categoryDao.create(category);
    }

    // =========================
    // UPDATE CATEGORY (ADMIN)
    // =========================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(
            @PathVariable int id,
            @Valid @RequestBody Category category)
    {
        categoryDao.update(id, category);
    }

    // =========================
    // DELETE CATEGORY (ADMIN)
    // =========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable int id)
    {
        categoryDao.delete(id);
    }
}
