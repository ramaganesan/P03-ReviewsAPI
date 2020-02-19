package com.udacity.course3.reviews.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDownVoteDto {

    private int downvoteCount;
}
