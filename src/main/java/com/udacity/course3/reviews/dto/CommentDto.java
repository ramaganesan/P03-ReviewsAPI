package com.udacity.course3.reviews.dto;

import com.udacity.course3.reviews.domain.Review;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private String commentId;

    @NotBlank(message = "CommentBody is mandatory")
    private String commentBody;

    private int upvoteCount;

    private int downVoteCount;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;
    

}
