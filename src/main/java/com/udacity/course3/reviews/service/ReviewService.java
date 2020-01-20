package com.udacity.course3.reviews.service;

import com.udacity.course3.reviews.controller.ReviewsController;
import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.domain.Review;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.Optional;

@Service
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ProductRepository productRepository;

    private final ReviewRepository reviewRepository;

    public ReviewService(@Autowired ProductRepository productRepository, @Autowired  ReviewRepository reviewRepository){
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Creates a review for a product.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */

    public Review createReviewForProduct(Integer productId, Review review){
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("No Product found for id: " + productId));
        review.setProduct(product);
        review = reviewRepository.save(review);
        return review;
    }

    public Collection<Review> listReviewsForProduct(Integer productId){
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("No Product found for id: " + productId));
        Collection<Review> reviews = reviewRepository.findByProductProductId(product.getProductId());
        return reviews;

    }
}
