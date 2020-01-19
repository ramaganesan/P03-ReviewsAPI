package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.domain.Comment;
import com.udacity.course3.reviews.domain.Review;
import com.udacity.course3.reviews.dto.CommentDto;
import com.udacity.course3.reviews.dto.ReviewObjectDto;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.repository.CommentsRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
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
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
@Slf4j
public class CommentsController {

    private final static Logger logger = LoggerFactory.getLogger(CommentsController.class);

    private final CommentsRepository commentsRepository;

    private final ReviewRepository reviewRepository;

    private final ModelMapper modelMapper;

    public CommentsController(@Autowired CommentsRepository commentsRepository, @Autowired ReviewRepository reviewRepository, @Autowired ModelMapper modelMapper){
        this.commentsRepository = commentsRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    // TODO: Wire needed JPA repositories here

    /**
     * Creates a comment for a review.
     *
     * 1. Add argument for comment entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, save comment.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.POST)
    public ResponseEntity<?> createCommentForReview(@PathVariable("reviewId") Integer reviewId, @Valid @RequestBody CommentDto commentDto) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElseThrow(() -> new ResourceNotFoundException("No review found for id: " + reviewId));
        Comment comment = convertToComment(commentDto);
        review.addComment(comment);
        comment = commentsRepository.save(comment);
        return new ResponseEntity<>(convertToCommentDto(comment),HttpStatus.CREATED);
    }

    /**
     * List comments for a review.
     *
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, return list of comments.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.GET)
    public ResponseEntity<?> listCommentsForReview(@PathVariable("reviewId") Integer reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElseThrow(() -> new ResourceNotFoundException("No review found for id: " + reviewId));
        Collection<Comment> commentCollection = commentsRepository.findByReviewReviewId(reviewId);
        Collection<CommentDto> commentDtos = commentCollection.stream().map(comment -> convertToCommentDto(comment)).collect(Collectors.toList());
        return new ResponseEntity(commentDtos,HttpStatus.OK);
    }

    private CommentDto convertToCommentDto(Comment comment){
        return modelMapper.map(comment,CommentDto.class);
    }

    private Comment convertToComment(CommentDto commentDto){
        return modelMapper.map(commentDto,Comment.class);
    }

    private ReviewObjectDto convertToReviewObjectDto(Review review){
        ReviewObjectDto reviewObjectDto = modelMapper.map(review,ReviewObjectDto.class);
        //reviewObjectDto.addCommentDto(review.getComments());
        return reviewObjectDto;
    }
}