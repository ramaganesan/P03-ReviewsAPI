package com.udacity.course3.reviews.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum     RatingsEnum {
    POOR(0), GOOD(1), EXCELLENT(2), UNKNOWN_RATING(-1);

    private int rating;

    private static final Map<Integer,RatingsEnum> INTEGER_RATINGS_ENUM_MAP;

    static {
        INTEGER_RATINGS_ENUM_MAP = Arrays.stream(values())
                                          .collect(Collectors.toMap(ratingsEnum -> ratingsEnum.rating, ratingsEnum -> ratingsEnum));
    }

    private RatingsEnum(int i) {
        this.rating = i;
    }

    public int getRating(){

        return this.rating;
    }

    public static RatingsEnum fromInteger(Integer value){
        return Optional.ofNullable(INTEGER_RATINGS_ENUM_MAP.get(value)).orElse(UNKNOWN_RATING);
    }
}
