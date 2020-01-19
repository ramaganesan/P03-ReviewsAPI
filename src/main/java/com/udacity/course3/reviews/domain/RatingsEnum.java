package com.udacity.course3.reviews.domain;

public enum RatingsEnum {
    POOR(0), GOOD(1), EXCELLENT(2);

    private int rating;

    RatingsEnum(int i) {
        this.rating = i;
    }

    public int getRating(){
        return this.rating;
    }
}
