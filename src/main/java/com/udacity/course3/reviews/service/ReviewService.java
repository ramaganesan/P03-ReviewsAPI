package com.udacity.course3.reviews.service;

import com.udacity.course3.reviews.document.CommentDocument;
import com.udacity.course3.reviews.document.ReviewDocument;
import com.udacity.course3.reviews.domain.Comment;
import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.domain.Review;
import com.udacity.course3.reviews.dto.*;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.repository.CommentsRepository;
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

    private final CommentsRepository commentsRepository;

    private final com.udacity.course3.reviews.mongorepository.ReviewRepository mongoReviewRepository;

    private final ModelMapper modelMapper;

    public ReviewService(@Autowired ProductRepository productRepository, @Autowired  ReviewRepository reviewRepository, @Autowired com.udacity.course3.reviews.mongorepository.ReviewRepository mongoReviewRepository,
                         @Autowired ModelMapper modelMapper, @Autowired CommentsRepository commentsRepository){
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.mongoReviewRepository = mongoReviewRepository;
        this.modelMapper = modelMapper;
        this.commentsRepository = commentsRepository;
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
    @Deprecated
    public ReviewDocument createReviewDocumentForProduct(Integer productId, ReviewDocument reviewDocument){
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("No Product found for id: " + productId));
        if(reviewDocument.getProductId() == 0)
            reviewDocument.setProductId(product.getProductId());
        reviewDocument = mongoReviewRepository.save(reviewDocument);
        return reviewDocument;
    }

    /**
     * Creates a review document in MongoDB for a product.
     *
     * @param productId The id of the product.
     * @param reviewObjectDto ReviewObjectDto
     * @return The created review or 404 if product id is not found.
     */
    public ReviewObjectDto createReviewForProductInRepositories(Integer productId, ReviewObjectDto reviewObjectDto) {
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(() -> new ResourceNotFoundException("No Product found for id: " + productId));
        Review review = ReviewApplicationUtils.convertReviewObjectDtoToReview(reviewObjectDto,modelMapper);
        review.setProduct(product);
        review = reviewRepository.save(review);
        try {
            ReviewDocument reviewDocument = ReviewApplicationUtils.convertReviewObjectDtoToReviewDocument(reviewObjectDto,modelMapper);
            reviewDocument.setReviewId(review.getReviewId());
            reviewDocument.setProductId(productId);
            reviewDocument = createReviewForProductInMongoDb(reviewDocument);
            reviewObjectDto = ReviewApplicationUtils.convertReviewDocumentToReviewObjectDto(reviewDocument,modelMapper);
        }catch (Exception e){
            logger.error("Exception while storing review document in DB" + e.getLocalizedMessage());
            reviewRepository.delete(review);
            throw e;
        }
        return reviewObjectDto;
    }



    /**
     * Return reviews for a product.
     * Get Reviews from  Mongo DB
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
        return reviewObjectDtos;

    }

    /**
     * Add Comments for a Review
     * @param reviewId Object Id
     * @return The Review with Comments or 404 if Review is not found. We
     */
    @Deprecated
    public ReviewObjectDto createCommentForReview(ObjectId reviewId, CommentDocument commentDocument){
        Optional<ReviewDocument> reviewDocumentOptional = mongoReviewRepository.findById(reviewId);
        ReviewDocument reviewDocument = reviewDocumentOptional.orElseThrow(() -> new ResourceNotFoundException("No Reviews found for id" + reviewId));
        commentDocument.set_id(new ObjectId());
        commentDocument.setCommentId(1);
        commentDocument.setCreateDate(LocalDateTime.now());
        commentDocument.setUpdateDate(LocalDateTime.now());
        reviewDocument.addComment(commentDocument);
        mongoReviewRepository.save(reviewDocument);
        return ReviewApplicationUtils.convertReviewDocumentToReviewObjectDto(reviewDocument,modelMapper);

    }

    /**
     * Add Comments for a Review
     * @param reviewId Integer
     * @param commentDto CommentDto
     * @return The Review with Comments or 404 if Review is not found.
     */
    public ReviewObjectDto createCommmentsForReviewInRepositories(Integer reviewId, CommentDto commentDto){
        Comment comment = addCommentsForReviewsInDb(reviewId, commentDto);
        Optional<ReviewDocument> optionalReviewDocument = mongoReviewRepository.findReviewDocumentByReviewId(reviewId);
        try{
           if(optionalReviewDocument.isPresent()) {
               ReviewDocument reviewDocument = optionalReviewDocument.get();
               CommentDocument commentDocument = ReviewApplicationUtils.getCommentDocumentFromComment(comment);
               reviewDocument = addCommentToReview(commentDocument,optionalReviewDocument.get().getReviewId());
               return ReviewApplicationUtils.convertReviewDocumentToReviewObjectDto(reviewDocument,modelMapper);
           }else{
               //Unable to find Review in MongoDB, so creating new one along with comments
               logger.info("Unable to find Review in MongoDB, so creating new one along with comments");
               ReviewDocument reviewDocument = convertMySQlReviewToReviewDocument(reviewId);
               reviewDocument = createReviewForProductInMongoDb(reviewDocument);
               return ReviewApplicationUtils.convertReviewDocumentToReviewObjectDto(reviewDocument,modelMapper);
           }
        }catch (Exception e){
            logger.error("Exception while storing review document in DB" + e.getLocalizedMessage());
            commentsRepository.delete(comment);
            throw e;
        }

    }



    /**
     *
     * @param reviewId
     * @return Review
     */
    public ReviewDocument findReviewDocument(Integer reviewId){
        Optional<ReviewDocument> reviewDocumentOptional = mongoReviewRepository.findReviewDocumentByReviewId(reviewId);
        ReviewDocument reviewDocument = reviewDocumentOptional.orElseThrow(() -> new ResourceNotFoundException("No Reviews found for id" + reviewId));
        return reviewDocument;
    }

    /**
     *
     * @param commentUpVoteDto
     * @param commentId
     * @return Updated CommentDocument
     */
    public long updateCommentDocumentUpVote(CommentUpVoteDto commentUpVoteDto, Integer commentId){
        Optional<Comment> commentOptional = commentsRepository.findById(commentId);
        Comment comment = commentOptional.orElseThrow(() -> new ResourceNotFoundException("No comment found for id" + commentId));
        int beforeUpdateUpVoteCount = comment.getDownVoteCount();
        comment.setUpvoteCount(beforeUpdateUpVoteCount + commentUpVoteDto.getUpvoteCount());
        comment = commentsRepository.save(comment);
        CommentDocument commentDocument = ReviewApplicationUtils.convertCommentUpVoteDtoToCommentDocument(commentUpVoteDto,modelMapper);
        commentDocument.setCommentId(commentId);
        try {
            long updateCount = mongoReviewRepository.updateCommentDocumentUpVote(commentDocument);
            return updateCount;
        }catch (Exception e){
            comment.setUpvoteCount(beforeUpdateUpVoteCount);
            commentsRepository.save(comment);
            throw e;
        }
    }/**
     *
     * @param commentDownVoteDto
     * @param commentId
     * @return Updated CommentDocument
     */
    public long updateCommentDocumentDownVote(CommentDownVoteDto commentDownVoteDto, Integer commentId){
        Optional<Comment> commentOptional = commentsRepository.findById(commentId);
        Comment comment = commentOptional.orElseThrow(() -> new ResourceNotFoundException("No comment found for id" + commentId));
        int beforeUpdateDownVoteCount = comment.getDownVoteCount();
        comment.setDownVoteCount(beforeUpdateDownVoteCount + commentDownVoteDto.getDownvoteCount());
        comment = commentsRepository.save(comment);
        CommentDocument commentDocument = ReviewApplicationUtils.convertCommentDownVoteDtoToCommentDocument(commentDownVoteDto,modelMapper);
        commentDocument.setCommentId(commentId);
        try {
            long updateCount = mongoReviewRepository.updateCommentDocumentDownVote(commentDocument);
            return updateCount;
        }catch (Exception e){
            comment.setDownVoteCount(beforeUpdateDownVoteCount);
            commentsRepository.save(comment);
            throw e;
        }
    }

    private void deleteCommentForReviewFromMySql(Integer commentId){
        Comment comment = Comment.builder().commentId(commentId).build();
        commentsRepository.delete(comment);
    }

    private void deleteReviewFromMySql(Integer reviewId){
        Review review = Review.builder().reviewId(reviewId).build();
        reviewRepository.delete(review);
    }

    private Comment addCommentsForReviewsInDb(Integer reviewId, CommentDto commentDto) {
        logger.info("Adding Comments for Review in MySQL");
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElseThrow(() -> new ResourceNotFoundException("No Reviews found for id" + reviewId));
        Comment comment = ReviewApplicationUtils.convertCommentDtoToComment(commentDto,modelMapper);

        review.addComment(comment);
        comment = commentsRepository.save(comment);
        return comment;
    }

    private ReviewDocument convertMySQlReviewToReviewDocument(Integer reviewId){
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElseThrow(() -> new ResourceNotFoundException("No Reviews found for id" + reviewId));
        ReviewDocument reviewDocument = ReviewApplicationUtils.convertReviewToReviewDocument(review,modelMapper);
        Collection<Comment> comments = commentsRepository.findByReviewReviewId(reviewId);
        comments.stream().forEach(comment -> {
            CommentDocument commentDocument = ReviewApplicationUtils.getCommentDocumentFromComment(comment);
            reviewDocument.addComment(commentDocument);
        });
        return reviewDocument;
    }

    private ReviewDocument createReviewForProductInMongoDb(ReviewDocument reviewDocument) {
        reviewDocument = mongoReviewRepository.save(reviewDocument);
        return reviewDocument;
    }

    private ReviewDocument addCommentToReview(CommentDocument commentDocument, Integer reviewId){
        ReviewDocument reviewDocument = mongoReviewRepository.addCommentToReview(commentDocument, reviewId);
        return reviewDocument;
    }


}
