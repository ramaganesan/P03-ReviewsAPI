package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.document.ReviewDocument;
import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.domain.Review;
import com.udacity.course3.reviews.dto.ProductDto;
import com.udacity.course3.reviews.dto.ReviewDto;
import com.udacity.course3.reviews.dto.ReviewObjectDto;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import com.udacity.course3.reviews.service.ProductService;
import com.udacity.course3.reviews.service.ReviewService;
import com.udacity.course3.reviews.utils.ReviewApplicationUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
@Slf4j
public class ReviewsController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewsController.class);

    private final ModelMapper modelMapper;

    private final ReviewService reviewService;

    public ReviewsController(@Autowired ModelMapper modelMapper, @Autowired ReviewService reviewService){
        this.modelMapper = modelMapper;
        this.reviewService = reviewService;
    }


    /**
     * Creates a review for a product.
     * <p>
     * 1. Add argument for review entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of product.
     * 3. If product not found, return NOT_FOUND.
     * 4. If found, save review.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.POST)
    public ResponseEntity<?> createReviewForProduct(@PathVariable("productId") Integer productId,@Valid @RequestBody ReviewObjectDto reviewObjectDto) {

        ReviewDocument reviewDocument = ReviewApplicationUtils.convertReviewObjectDtosToReviewDocument(reviewObjectDto,modelMapper);

        reviewDocument = reviewService.createReviewDocumentForProduct(productId,reviewDocument);
        return new ResponseEntity<>(ReviewApplicationUtils.convertReviewDocumentToReviewObjectDto(reviewDocument,modelMapper),HttpStatus.CREATED);

    }

    /**
     * Lists reviews by product.
     *
     * @param productId The id of the product.
     * @return The list of reviews.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<Collection<ReviewObjectDto>> listReviewsForProduct(@PathVariable("productId") Integer productId,@RequestParam(value = "pageNum", required = true) Integer pageNum, @RequestParam(value = "numElements", required = true) Integer numElements) {
        Collection<ReviewObjectDto> reviewObjectDtos = reviewService.listReviewsForProduct(productId, pageNum, numElements);
        return new ResponseEntity<>(reviewObjectDtos,HttpStatus.OK);
    }

    private Review convertToReview(ReviewObjectDto reviewObjectDto){
        return modelMapper.map(reviewObjectDto, Review.class);
    }

    private ReviewObjectDto convertToReviewObjectDto(Review review){
        ReviewObjectDto reviewObjectDto = modelMapper.map(review,ReviewObjectDto.class);
        //reviewObjectDto.addCommentDto(review.getComments());
        return reviewObjectDto;
    }


}