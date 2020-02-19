package com.udacity.course3.reviews.dto;

import com.udacity.course3.reviews.document.CommentDocument;
import com.udacity.course3.reviews.domain.RatingsEnum;
import lombok.*;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewObjectDto {

    private Integer reviewId;

    private String _id;

    private RatingsEnum reviewRating;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "ReviewBody is mandatory")
    private String reviewBody;

    @NotBlank(message = "Author is mandatory")
    private String author;

    private String publisher;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private List<CommentDocument> commentDocuments;

}
