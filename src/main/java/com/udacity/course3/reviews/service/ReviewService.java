package com.udacity.course3.reviews.service;

import com.udacity.course3.reviews.document.CommentDocument;
import com.udacity.course3.reviews.document.ReviewDocument;
import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.domain.Review;
import com.udacity.course3.reviews.dto.ReviewObjectDto;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import com.udacity.course3.reviews.utils.ReviewApplicationUtils;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ProductRepository productRepository;

    private final ReviewRepository reviewRepository;

    private final com.udacity.course3.reviews.mongorepository.ReviewRepository mongoReviewRepository;

    private final ModelMapper modelMapper;

    public ReviewService(@Autowired ProductRepository productRepository, @Autowired  ReviewRepository reviewRepository, @Autowired com.udacity.course3.reviews.mongorepository.ReviewRepository mongoReviewRepository, @Autowired ModelMapper modelMapper){
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.mongoReviewRepository = mongoReviewRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a review for a product.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */

    @Deprecated
    public Review createReviewForProduct(Integer productId, Review review){
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("No Product found for id: " + productId));
        review.setProduct(product);
        review = reviewRepository.save(review);
        return review;
    }
    /**
     * Creates a review document in MongoDB for a product.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    public ReviewDocument createReviewDocumentForProduct(Integer productId, ReviewDocument reviewDocument){
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("No Product found for id: " + productId));
        if(reviewDocument.getProductId() == 0)
            reviewDocument.setProductId(product.getProductId());
        reviewDocument = mongoReviewRepository.save(reviewDocument);
        return reviewDocument;
    }

    /**
     * Return reviews for a product.
     * Get Reviews both from the MySql DB and Mongo DB
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    public Collection<ReviewObjectDto> listReviewsForProduct(Integer productId, Integer pageNum, Integer numElements){
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("No Product found for id: " + productId));
        Pageable pageable = PageRequest.of(pageNum, numElements);
        Collection<ReviewObjectDto> reviewObjectDtos = new ArrayList<>();
        List<ReviewDocument> reviewDocuments = mongoReviewRepository.findReviewDocumentByProductId(productId,pageable);
        ReviewApplicationUtils.convertReviewDocumentsToReviewObjectsDTO(reviewObjectDtos,reviewDocuments,modelMapper);
        Collection<Review> reviews = reviewRepository.findByProductProductId(product.getProductId(), pageable);
        ReviewApplicationUtils.convertReviewsToReviewObjectsDTO(reviewObjectDtos,reviews,modelMapper);

        return reviewObjectDtos;

    }

    /**
     * Add Comments for a Review
     * @param reviewId Object Id
     * @return The Review with Comments or 404 if Review is not found. We
     */
    public ReviewObjectDto createCommentForReview(ObjectId reviewId, CommentDocument commentDocument){
        Optional<ReviewDocument> reviewDocumentOptional = mongoReviewRepository.findById(reviewId);
        ReviewDocument reviewDocument = reviewDocumentOptional.orElseThrow(() -> new ResourceNotFoundException("No Reviews found for id" + reviewId));
        commentDocument.set_id(new ObjectId());
        commentDocument.setCreateDate(LocalDateTime.now());
        commentDocument.setUpdateDate(LocalDateTime.now());
        reviewDocument.addComment(commentDocument);
        mongoReviewRepository.save(reviewDocument);
        return ReviewApplicationUtils.convertReviewDocumentToReviewObjectDto(reviewDocument,modelMapper);

    }

    /**
     *
     * @param reviewId
     * @return Review
     */
    public ReviewDocument findReviewDocument(ObjectId reviewId){
        Optional<ReviewDocument> reviewDocumentOptional = mongoReviewRepository.findById(reviewId);
        ReviewDocument reviewDocument = reviewDocumentOptional.orElseThrow(() -> new ResourceNotFoundException("No Reviews found for id" + reviewId));
        return reviewDocument;
    }

    /**
     *
     * @param commentDocument
     * @return Updated CommentDocument
     */
    public long updateCommentDocumentUpVote(CommentDocument commentDocument){
        long updateCount = mongoReviewRepository.updateCommentDocumentUpVote(commentDocument);
        return updateCount;
    }/**
     *
     * @param commentDocument
     * @return Updated CommentDocument
     */
    public long updateCommentDocumentDownVote(CommentDocument commentDocument){
        long updateCount = mongoReviewRepository.updateCommentDocumentDownVote(commentDocument);
        return updateCount;
    }

}
