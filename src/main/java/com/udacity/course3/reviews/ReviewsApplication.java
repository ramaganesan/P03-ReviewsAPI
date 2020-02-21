package com.udacity.course3.reviews;

import com.udacity.course3.reviews.document.CommentDocument;
import com.udacity.course3.reviews.document.ReviewDocument;
import com.udacity.course3.reviews.domain.Comment;
import com.udacity.course3.reviews.dto.CommentDto;
import com.udacity.course3.reviews.dto.ReviewObjectDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReviewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewsApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<ReviewDocument, ReviewObjectDto> reviewDocumentReviewObjectDtoTypeMap = modelMapper.createTypeMap(ReviewDocument.class,ReviewObjectDto.class);
		reviewDocumentReviewObjectDtoTypeMap.addMapping( reviewDocument-> reviewDocument.get_id(), ReviewObjectDto::set_id);
		reviewDocumentReviewObjectDtoTypeMap.addMapping(reviewDocument -> reviewDocument.getReviewRating(),ReviewObjectDto::setReviewRating);


		TypeMap<CommentDocument, CommentDto> commentDocumentCommentDtoTypeMap = modelMapper.createTypeMap(CommentDocument.class,CommentDto.class);
		commentDocumentCommentDtoTypeMap.addMapping(commentDocument -> commentDocument.get_id(),CommentDto::set_id);
		commentDocumentCommentDtoTypeMap.addMapping(commentDocument -> commentDocument.getCommentId(),CommentDto::setCommentId);

		TypeMap<CommentDto, Comment> commentDtoCommentTypeMap = modelMapper.createTypeMap(CommentDto.class, Comment.class);
		commentDtoCommentTypeMap.addMappings(modelMapper1 -> modelMapper1.skip(Comment::setCommentId));

		return modelMapper;
	}

}