package com.ecommerce.jideBrand.controller;

import com.ecommerce.jideBrand.model.Category;
import com.ecommerce.jideBrand.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;



    @GetMapping("/public/categories")
//    @RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/public/createCategory")
//    @RequestMapping(value = "/public/createCategory", method = RequestMethod.POST)
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category){
       try {
           categoryService.createCategory(category);

           return new  ResponseEntity<>("Category Successfully Created", HttpStatus.CREATED);
       }
       catch (ResponseStatusException e){
           return new ResponseEntity<>(e.getReason(), e.getStatusCode());
       }

    }

    @DeleteMapping("/admin/deleteCategory/{categoryId}")
//    @RequestMapping(value = "/admin/deleteCategory/{categoryId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        try {
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/admin/updateCategory/{categoryId}")
//    @RequestMapping(value = "/admin/updateCategory/{categoryId}", method = RequestMethod.PUT)
    public ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable Long categoryId) {
        try {
            Category savedCategory = categoryService.updateCategory(categoryId, category);
            return new ResponseEntity<>(savedCategory, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(null, e.getStatusCode()); // You can't return a String in a ResponseEntity<Category>
        }
    }

}
