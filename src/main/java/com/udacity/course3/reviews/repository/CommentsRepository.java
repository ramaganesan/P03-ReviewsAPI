package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.domain.Comment;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collection;

public interface CommentsRepository extends CrudRepository<Comment, Integer> {

    Collection<Comment> findByReviewReviewId(Integer reviewId);
}
