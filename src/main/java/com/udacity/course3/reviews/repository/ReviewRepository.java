package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.domain.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {

    Collection<Review> findByProductProductId(Integer productId);
}
