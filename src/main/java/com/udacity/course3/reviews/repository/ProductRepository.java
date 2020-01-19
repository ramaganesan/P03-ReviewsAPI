package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.domain.Review;
import com.udacity.course3.reviews.dto.ReviewDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query(value = "select r.review_id as reviewId , r.review_rating as reviewRating,r.name as name,r.review_body as reviewBody,r.author as author,r.publisher as publisher,r.product_id as productId from product p join review r on p.product_id = r.product_id and p.product_id = :product_id" , nativeQuery = true)
    Collection<ReviewDto> getReviewsForProduct(@Param("product_id") Integer productId);
}
