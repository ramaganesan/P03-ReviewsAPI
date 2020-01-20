package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.dto.ProductDto;
import com.udacity.course3.reviews.dto.ReviewDto;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring REST controller for working with product entity.
 */
@RestController
@RequestMapping("/products")
@Slf4j
public class ProductsController {

    // TODO: Wire JPA repositories here
   private static final Logger logger = LoggerFactory.getLogger(ProductsController.class);

    private final ProductService productService;

    private final ModelMapper modelMapper;

    public  ProductsController(@Autowired ProductService productService, @Autowired ModelMapper modelMapper){
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a product.
     *
     * 1. Accept product as argument. Use {@link RequestBody} annotation.
     * 2. Save product.
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
      Product product = convertToProduct(productDto);
      product = productService.createProduct(product);
      productDto = convertToProductDto(product,null);
      return new ResponseEntity<>(productDto, HttpStatus.CREATED);

    }

    /**
     * Finds a product by id.
     *
     * @param id The id of the product.
     * @return The product if found, or a 404 not found.
     */
    @RequestMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id, @RequestParam(value = "includeReview", required = false) boolean includeReview) throws ResourceNotFoundException{
        Product product = productService.findById(id);
        Collection<ReviewDto> reviewDtos = new ArrayList<>();
        if(includeReview == true) {
            logger.info("Required to get Reviews for the product as well");
            reviewDtos.addAll(productService.getReviewsForProduct(product.getProductId()));
        }
        return new ResponseEntity(convertToProductDto(product,reviewDtos), HttpStatus.OK);
    }

    /**
     * Lists all products.
     *
     * @return The list of products.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<?> listProducts() {
      return productService.listProducts();

    }

    private ProductDto convertToProductDto(Product product, Collection<ReviewDto> reviewDtos){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        if(reviewDtos != null && !reviewDtos.isEmpty()){
            productDto.setReviewDtos(reviewDtos);
        }
        return productDto;
    }

    private Product convertToProduct(ProductDto productDto){
        Product product = modelMapper.map(productDto,Product.class);
        return product;
    }
}