package com.udacity.course3.reviews.dto;

import com.udacity.course3.reviews.domain.Comment;
import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.domain.RatingsEnum;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewObjectDto {

    private Integer reviewId;

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

    /*private List<CommentDto> commentDtos = new ArrayList<>();

    public void addCommentDto(Collection<Comment> comments){
        comments.forEach(comment -> {
            CommentDto commentDto = CommentDto.builder().commentBody(comment.getCommentBody()).upvoteCount(comment.getUpvoteCount()).commentId(comment.getCommentId())
                    .downVoteCount(comment.getDownVoteCount()).createDate(comment.getCreateDate()).updateDate(comment.getUpdateDate())
                    .build();
            this.commentDtos.add(commentDto);
        });

    }*/

}
