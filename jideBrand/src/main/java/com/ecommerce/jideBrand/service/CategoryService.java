package com.ecommerce.jideBrand.service;

import com.ecommerce.jideBrand.model.Category;
import com.ecommerce.jideBrand.payload.CategoryDTO;
import com.ecommerce.jideBrand.payload.CategoryResponse;



public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

   CategoryDTO createCategory(CategoryDTO category);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
}
