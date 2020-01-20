package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReviewRepository extends PagingAndSortingRepository<Review, Integer> {

    List<Review> findByProductProductId(Integer productId, Pageable pageable);
}
