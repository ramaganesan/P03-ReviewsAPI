package com.udacity.course3.reviews.mongorepository;

import com.udacity.course3.reviews.document.CommentDocument;
import com.udacity.course3.reviews.document.ReviewDocument;
import com.udacity.course3.reviews.domain.RatingsEnum;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;


public interface ReviewRepository extends MongoRepository<ReviewDocument, ObjectId>, ReviewRepositoryComment {
    List<ReviewDocument> findReviewDocumentByProductId(Integer productId, Pageable pageable);

    Optional<ReviewDocument> findReviewDocumentByReviewId(Integer reviewId);

    List<ReviewDocument> findReviewDocumentByProductIdAndReviewRating(Integer productId, RatingsEnum ratingsEnum);


}
