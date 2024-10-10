package com.ecommerce.jideBrand.service;

import com.ecommerce.jideBrand.exceptions.APIException;
import com.ecommerce.jideBrand.exceptions.ResourceNotFoundException;
import com.ecommerce.jideBrand.model.Category;
import com.ecommerce.jideBrand.model.Product;
import com.ecommerce.jideBrand.payload.ProductDTO;
import com.ecommerce.jideBrand.payload.ProductResponse;
import com.ecommerce.jideBrand.repositories.CategotyRepository;
import com.ecommerce.jideBrand.repositories.ProductRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CategotyRepository categotyRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;
    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
//        check if productName is already present

//        first find the category id and if it doesn't exist throw the appopriate error response
        Category category = categotyRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Caregory", "CategoryID", categoryId));
        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if(isProductNotPresent){
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("default.png");
            product.setCategory(category);
//calculate the special price and set it
            double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
            product.setSpecialPrice(specialPrice);
//        save the product
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }else {
            throw new APIException("product already exist");
        }


    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy)
                .ascending()
                :Sort.by(sortBy)
                .descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
     Page<Product> pageProducts=  productRepository.findAll(pageDetails);
     List<Product> products = pageProducts.getContent();
     List<ProductDTO> productDTOS = products.stream()
             .map(product -> modelMapper.map(product, ProductDTO.class))
             .toList();
     if(products.isEmpty()){
         throw new APIException("No Product Found");
     }
     ProductResponse productResponse = new ProductResponse();
     productResponse.setContents(productDTOS);
     productResponse.setPageNumber(pageProducts.getNumber());
     productResponse.setPageSize(pageProducts.getSize());
     productResponse.setTotalPages(pageProducts.getTotalPages());
     productResponse.setTotalElements(pageProducts.getTotalElements());
     productResponse.setLastPage(pageProducts.isLast());

     return productResponse;

    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber,
                                            Integer pageSize, String sortOrder, String sortBy) {
//        get the categoryId
       Category category = categotyRepository.findById(categoryId)
               .orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));
//       get the list of the products in the productRepository by finding it by categoryId
        Sort sortByAndOrder =sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy)
                .ascending()
                :Sort.by(sortBy)
                .descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);
       List<Product> products = pageProducts.getContent();
       List<ProductDTO> productDTOS = products.stream()
               .map(product -> modelMapper.map(product, ProductDTO.class))
               .toList();
//       set it to the existing model response
        if(productDTOS.isEmpty()){
            throw new APIException("No product Found in category" + category);
        }
       ProductResponse productResponse = new ProductResponse();
       productResponse.setContents(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
       return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyWord(String keyWord, Integer pageNumber,
                                                  Integer pageSize, String sortOrder, String sortBy) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy)
                .ascending()
                :Sort.by(sortBy)
                .descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase('%' + keyWord + '%', pageDetails );
    List<Product> products = pageProducts.getContent();
    List<ProductDTO> productDTOS = products.stream()
            .map(product -> modelMapper.map(product, ProductDTO.class))
            .toList();

    if(products.isEmpty()){
        throw new APIException("products not found with keyword "+ keyWord);
    }
    ProductResponse productResponse = new ProductResponse();
    productResponse.setContents(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO product) {
//        get the product from the DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
//        update the product with the one coming from the user
       productFromDb.setProductName(product.getProductName());
       productFromDb.setDescription(productFromDb.getDescription());
       productFromDb.setDiscount(product.getDiscount());
       productFromDb.setQuantity(product.getQuantity());
       productFromDb.setPrice(product.getPrice());
       productFromDb.setSpecialPrice(product.getSpecialPrice());
      Product savedProduct = productRepository.save(productFromDb);
      return modelMapper.map(savedProduct, ProductDTO.class);


    }

    @Override
    public ProductDTO deleteCategory(Long productId) {
        Product deletedProduct = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product", "productId", productId));
        productRepository.delete((deletedProduct));
        return modelMapper.map(deletedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
    Product productFromDb = productRepository.findById(productId)
            .orElseThrow(()-> new ResourceNotFoundException("product", "productId", productId));
//    upload image from the server

        String fileName =  fileService.uploadImage(path, image);

//        updating the image in the db
        productFromDb.setImage(fileName);
//        save the updated product
        Product updatedProduct = productRepository.save(productFromDb);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }


}
