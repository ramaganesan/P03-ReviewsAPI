package com.udacity.course3.reviews.dto;

import com.udacity.course3.reviews.domain.RatingsEnum;

public interface ReviewDto {
    String getName();
    String getReviewBody();
    String getAuthor();
    String getPublisher();
    Integer getReviewId();
    Integer getProductId();
    RatingsEnum getReviewRating();

}
