package com.udacity.course3.reviews.utils;

import com.udacity.course3.reviews.document.CommentDocument;
import com.udacity.course3.reviews.document.ReviewDocument;
import com.udacity.course3.reviews.domain.Comment;
import com.udacity.course3.reviews.domain.Review;
import com.udacity.course3.reviews.dto.CommentDto;
import com.udacity.course3.reviews.dto.ReviewDto;
import com.udacity.course3.reviews.dto.ReviewObjectDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class ReviewApplicationUtils {

    public static void convertReviewsToReviewObjectsDTO(Collection<ReviewObjectDto> reviewObjectDtos, Collection<Review> reviews, ModelMapper modelMapper){
        Type reviewObjectDtosType = new TypeToken<Collection<ReviewObjectDto>>(){}.getType();
        if(reviews != null && !reviews.isEmpty())
            reviewObjectDtos.addAll(modelMapper.map(reviews,reviewObjectDtosType));
    }
    public static void convertReviewDocumentsToReviewObjectsDTO(Collection<ReviewObjectDto> reviewObjectDtos, List<ReviewDocument> reviewDocuments, ModelMapper modelMapper){
        Type reviewObjectDtosType = new TypeToken<Collection<ReviewObjectDto>>(){}.getType();
        if(reviewDocuments != null && !reviewDocuments.isEmpty())
            reviewObjectDtos.addAll(modelMapper.map(reviewDocuments,reviewObjectDtosType));
    }

    public static void convertReviewObjectsToReviewObjectsDTO(Collection<ReviewObjectDto> reviewObjectDtos, Collection<ReviewDto> reviewDtos, ModelMapper modelMapper){
        if(reviewDtos != null && !reviewDtos.isEmpty()){

            Type reviewObjectDtosType = new TypeToken<Collection<ReviewObjectDto>>(){}.getType();
            reviewObjectDtos.addAll(modelMapper.map(reviewDtos,reviewObjectDtosType));

        }

    }

    public static void convertCommentDocumnetsToCommentDTO(Collection<CommentDocument> commentDocuments, Collection<CommentDto> commentDtos, ModelMapper modelMapper){
        if (commentDocuments != null && !commentDocuments.isEmpty()){
            Type commentDtoObjectType = new TypeToken<Collection<CommentDto>>(){}.getType();
            commentDtos.addAll(modelMapper.map(commentDtos,commentDtoObjectType));
        }
    }

    public static ReviewDocument convertReviewObjectDtosToReviewDocument(ReviewObjectDto reviewObjectDto, ModelMapper modelMapper){
        return  modelMapper.map(reviewObjectDto,ReviewDocument.class);
    }

    public static ReviewObjectDto convertReviewDocumentToReviewObjectDto(ReviewDocument reviewDocument, ModelMapper modelMapper){
        return modelMapper.map(reviewDocument,ReviewObjectDto.class);
    }

    public static CommentDto convertCommentToCommentDTO(Comment comment, ModelMapper modelMapper){
        return modelMapper.map(comment,CommentDto.class);
    }

    public static CommentDto convertCommentDocumentToCommentDTO(CommentDocument commentDocument, ModelMapper modelMapper){
        return modelMapper.map(commentDocument,CommentDto.class);
    }

    public static CommentDocument convertCommentDtoToCommentDocument(CommentDto commentDto, ModelMapper modelMapper){
        return modelMapper.map(commentDto,CommentDocument.class);
    }
}
