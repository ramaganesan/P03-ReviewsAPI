package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.document.CommentDocument;
import com.udacity.course3.reviews.document.ReviewDocument;
import com.udacity.course3.reviews.domain.Comment;
import com.udacity.course3.reviews.domain.Review;
import com.udacity.course3.reviews.dto.CommentDto;
import com.udacity.course3.reviews.dto.ReviewObjectDto;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.repository.CommentsRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import com.udacity.course3.reviews.service.ReviewService;
import com.udacity.course3.reviews.utils.ReviewApplicationUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
@Slf4j
public class CommentsController {

    private final static Logger logger = LoggerFactory.getLogger(CommentsController.class);

    private final ReviewService reviewService;

    private final ModelMapper modelMapper;

    public CommentsController( @Autowired ReviewService reviewService, @Autowired ModelMapper modelMapper){
        this.reviewService = reviewService;
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
    public ResponseEntity<?> createCommentForReview(@PathVariable("reviewId") ObjectId reviewId, @Valid @RequestBody CommentDto commentDto) {
        CommentDocument commentDocument = ReviewApplicationUtils.convertCommentDtoToCommentDocument(commentDto,modelMapper);
        ReviewDocument reviewDocument = reviewService.createCommentForReview(reviewId,commentDocument);
        return new ResponseEntity<>(reviewDocument,HttpStatus.CREATED);
    }

    /**
     * List comments for a review.
     *
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, return list of comments.
     *
     * @param reviewId The Objectid of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.GET)
    public ResponseEntity<?> listCommentsForReview(@PathVariable("reviewId") ObjectId reviewId) {
        ReviewDocument reviewDocument = reviewService.findReviewDocument(reviewId);
        Collection<CommentDto> commentDtos = new ArrayList<>();
        ReviewApplicationUtils.convertCommentDocumnetsToCommentDTO(reviewDocument.getCommentDocuments(),commentDtos,modelMapper);
        return new ResponseEntity(commentDtos,HttpStatus.OK);
    }

    /**
     *
     * @param commentId
     * @param commentDto
     * @return HttpStatus
     */
    public ResponseEntity<?> updateComments(@PathVariable("commentId") ObjectId commentId, @Valid @RequestBody CommentDto commentDto){
        CommentDocument commentDocument = ReviewApplicationUtils.convertCommentDtoToCommentDocument(commentDto,modelMapper);
        commentDocument.set_id(commentId);
        reviewService.updateCommentDocument(commentDocument);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


}