package com.ecommerce.jideBrand.service;

import com.ecommerce.jideBrand.model.Category;
import com.ecommerce.jideBrand.repositories.CategotyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
//    private List<Category> categories = new ArrayList<>();
    @Autowired
    private CategotyRepository categotyRepository;
    @Override
    public List<Category> getAllCategories() {
        return categotyRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categotyRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category savedCategory = categotyRepository.findById(categoryId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
//delete the category
        categotyRepository.delete(savedCategory);
        return "Category successfully deleted";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {

        Category savedCategory = categotyRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

        category.setCategoryID(categoryId);
        savedCategory =categotyRepository.save(category);
        return savedCategory;
    }

}
