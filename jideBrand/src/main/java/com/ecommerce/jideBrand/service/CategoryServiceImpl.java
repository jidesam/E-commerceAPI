package com.ecommerce.jideBrand.service;

import com.ecommerce.jideBrand.exceptions.APIException;
import com.ecommerce.jideBrand.exceptions.ResourceNotFoundException;
import com.ecommerce.jideBrand.model.Category;
import com.ecommerce.jideBrand.payload.CategoryDTO;
import com.ecommerce.jideBrand.payload.CategoryResponse;
import com.ecommerce.jideBrand.repositories.CategotyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String soryBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(soryBy)
                .ascending()
                : Sort.by(soryBy)
                .descending();
//        pagiination
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categotyRepository.findAll(pageDetails);
        List<Category> categories =categoryPage.getContent();

        if(categories.isEmpty()){
            throw new APIException("No Category Found");
        }
//        rebuilding the response
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDB = categotyRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDB != null){
            throw new APIException("Category with this name "+ category.getCategoryName() + "already exist !!!");
        }
      Category savedCategory =  categotyRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category savedCategory = categotyRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));
//delete the category
        categotyRepository.delete(savedCategory);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {

        Category savedCategory = categotyRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryID(categoryId);

        savedCategory =categotyRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

}
