package com.udacity.course3.reviews.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.udacity.course3.reviews.mongorepository")
public class MongoDBConfiguration {

    @Bean
    public MongoCustomConversions mongoCustomConversions(){
        List<Converter> converters = new ArrayList<>();
        converters.add(new RatingsEnumReadingConverter());
        converters.add(new RatingsEnumWritingConverter());
        return new MongoCustomConversions(converters);
    }
}
