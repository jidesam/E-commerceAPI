package com.ecommerce.jideBrand.service;


import com.ecommerce.jideBrand.payload.ProductDTO;
import com.ecommerce.jideBrand.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);

    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);

    ProductResponse searchProductByKeyWord(String keyWord, Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);

    ProductDTO updateProduct(Long productId, ProductDTO product);

    ProductDTO deleteCategory(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
