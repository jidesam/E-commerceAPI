package com.ecommerce.jideBrand.controller;

import com.ecommerce.jideBrand.config.AppConstants;
import com.ecommerce.jideBrand.model.Product;
import com.ecommerce.jideBrand.payload.ProductDTO;
import com.ecommerce.jideBrand.payload.ProductResponse;
import com.ecommerce.jideBrand.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDto, @PathVariable Long categoryId){
      ProductDTO savedProductDTO =  productService.addProduct(categoryId, productDto);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/getAllProducts")
    public ResponseEntity<ProductResponse> getProducts(
            @RequestParam(name ="pageNumber", defaultValue = AppConstants.pageNumber, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.pageSize, required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.sortDIR,required = false) String sortOrder,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.sortProductsBy, required = false) String sortBy){
       ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortOrder, sortBy);
       return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/category/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name ="pageNumber", defaultValue = AppConstants.pageNumber, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize",defaultValue = AppConstants.pageSize, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.sortDIR,required = false) String sortOrder,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.sortProductsBy, required = false) String sortBy){
        ProductResponse productResponse = productService.searchByCategory(categoryId,  pageNumber,  pageSize,  sortOrder,  sortBy);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/products/keyword/{keyWord}")
    public ResponseEntity<ProductResponse> getProductsByKeyWord(@PathVariable String keyWord,
                                                                @RequestParam(name ="pageNumber", defaultValue = AppConstants.pageNumber, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize",defaultValue = AppConstants.pageSize, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.sortDIR,required = false) String sortOrder,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.sortProductsBy, required = false) String sortBy){
        ProductResponse productResponse = productService.searchProductByKeyWord(keyWord,  pageNumber,  pageSize,  sortOrder,  sortBy);
        return  new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public  ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductDTO productDTO){
        ProductDTO updatedProductDto = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProductDto, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO deleteProduct = productService.deleteCategory(productId);
        return  new ResponseEntity<>(deleteProduct, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image")MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage
                (productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

}
