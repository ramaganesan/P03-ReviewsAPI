package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.dto.ProductDto;
import com.udacity.course3.reviews.dto.ReviewDto;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.repository.ProductRepository;
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

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    public  ProductsController(@Autowired ProductRepository productRepository, @Autowired ModelMapper modelMapper){
        this.productRepository = productRepository;
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
      product = productRepository.save(product);
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
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("No Product found for id: " + id));
        Collection<ReviewDto> reviewDtos = new ArrayList<>();
        if(includeReview == true) {
            logger.info("Required to get Reviews for the product as well");
            reviewDtos.addAll(productRepository.getReviewsForProduct(product.getProductId()));
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
       List<Product> products = new ArrayList<>();
       Iterable<Product> productIterable = productRepository.findAll();
       productIterable.forEach(product -> products.add(product));
       return products;

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