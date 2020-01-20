package com.udacity.course3.reviews.service;

import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.dto.ReviewDto;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;



    public ProductService(@Autowired ProductRepository productRepository ){
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product){
        return productRepository.save(product);

    }

    public Product findById(Integer id){
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("No Product found for id: " + id));
        return  product;
    }

    public Collection<ReviewDto> getReviewsForProduct(Integer productId, Integer pageNum, Integer numElements){
        Pageable pageable = PageRequest.of(pageNum,numElements);
        return productRepository.getReviewsForProduct(productId,pageable);
    }

    public List<Product> listProducts(){
        List<Product> products = new ArrayList<>();
        Iterable<Product> productIterable = productRepository.findAll();
        productIterable.forEach(product -> products.add(product));
        return products;
    }
}
