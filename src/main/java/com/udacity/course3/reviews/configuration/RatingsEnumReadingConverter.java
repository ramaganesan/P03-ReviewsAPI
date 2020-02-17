package com.udacity.course3.reviews.configuration;

import com.udacity.course3.reviews.domain.RatingsEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class RatingsEnumReadingConverter implements Converter<Integer, RatingsEnum> {
    @Override
    public RatingsEnum convert(Integer i) {
        return RatingsEnum.fromInteger(i);
    }
}
