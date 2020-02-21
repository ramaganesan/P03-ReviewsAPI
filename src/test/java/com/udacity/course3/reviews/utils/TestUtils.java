package com.udacity.course3.reviews.utils;

import com.udacity.course3.reviews.document.CommentDocument;
import com.udacity.course3.reviews.document.ReviewDocument;
import com.udacity.course3.reviews.domain.Comment;
import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.domain.RatingsEnum;
import com.udacity.course3.reviews.domain.Review;
import org.bson.types.ObjectId;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.concurrent.atomic.AtomicInteger;

public class TestUtils {

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static Integer getNextNumber(){
        return counter.incrementAndGet();
    }

    public static ReviewDocument createReviewDocument(Integer productId, RatingsEnum ratingsEnum, String name, String reviewBody, String author, String publisher){
        ReviewDocument reviewDocument = ReviewDocument.builder().productId(productId).reviewRating(ratingsEnum).name(name).reviewBody(reviewBody)
                .author(author).publisher(publisher).build();
        reviewDocument.setReviewId(getNextNumber());
        return reviewDocument;
    }

    public static CommentDocument createCommentsForReview(String commentBody, Integer upvoteCount, Integer downVoteCount,Integer commentId){
        CommentDocument commentDocument = CommentDocument.builder().commentBody(commentBody).upvoteCount(upvoteCount).
                downVoteCount(downVoteCount).commentId(commentId)._id(new ObjectId()).build();
        return  commentDocument;
    }

    public static Comment buildComment(String commentBody, Integer upVoteCount, Integer downVoteCount, Review review){
        Comment comment = Comment.builder().commentBody(commentBody).upvoteCount(upVoteCount).downVoteCount(downVoteCount).review(review).build();
        return comment;
    }

    public static Product getProduct(String name, String description, String image){
        Product product = Product.builder().name(name).description(description).image(image).build();
        return product;
    }

    public static Review getReview(RatingsEnum ratingsEnum, String reviewName, String reviewBody, String author, String publisher, Product product){

        Review review = Review.builder().reviewRating(ratingsEnum).name(reviewName).reviewBody(reviewBody).author(author).publisher(publisher).product(product).build();
        return review;
    }
}
