package com.ecommerce.jideBrand.service;

import com.ecommerce.jideBrand.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

   void  createCategory(Category category);

    String deleteCategory(Long categoryId);

    Category updateCategory(Long categoryId, Category category);
}
