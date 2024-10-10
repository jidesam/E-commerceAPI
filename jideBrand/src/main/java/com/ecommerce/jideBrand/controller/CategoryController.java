package com.ecommerce.jideBrand.controller;

import com.ecommerce.jideBrand.config.AppConstants;
import com.ecommerce.jideBrand.model.Category;
import com.ecommerce.jideBrand.payload.CategoryDTO;
import com.ecommerce.jideBrand.payload.CategoryResponse;
import com.ecommerce.jideBrand.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

//@GetMapping("/echo")
//public ResponseEntity<String> echo(@RequestParam(name = "message", required = false)String message){
//    return new ResponseEntity<>("Echoed " + message + "!!!", HttpStatus.OK);
//}

    @GetMapping("/public/categories")
//    @RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.pageNumber, required = false) Integer pageNumber,
                                                             @RequestParam(name = "pageSize", defaultValue = AppConstants.pageSize, required = false) Integer pageSize,
                                                             @RequestParam(name = "sortBy", defaultValue = AppConstants.sortCategoriesBy, required = false) String sortBy,
                                                             @RequestParam(name = "sortOrder", defaultValue = AppConstants.sortDIR, required = false) String sortOrder){
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/public/createCategory")
//    @RequestMapping(value = "/public/createCategory", method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO category){
          CategoryDTO savedCategory = categoryService.createCategory(category);
           return new  ResponseEntity<>( savedCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/deleteCategory/{categoryId}")
//    @RequestMapping(value = "/admin/deleteCategory/{categoryId}", method = RequestMethod.DELETE)
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
            CategoryDTO deleteCategory = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(deleteCategory, HttpStatus.OK);
        }


    @PutMapping("/admin/updateCategory/{categoryId}")
//    @RequestMapping(value = "/admin/updateCategory/{categoryId}", method = RequestMethod.PUT)
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {
        CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }



}
