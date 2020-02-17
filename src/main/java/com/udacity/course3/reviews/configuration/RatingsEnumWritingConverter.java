package com.udacity.course3.reviews.configuration;

import com.udacity.course3.reviews.domain.RatingsEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class RatingsEnumWritingConverter implements Converter<RatingsEnum,Integer> {

    @Override
    public Integer convert(RatingsEnum ratingsEnum) {
        return new Integer(ratingsEnum.getRating());
    }
}
