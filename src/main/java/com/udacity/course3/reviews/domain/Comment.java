package com.udacity.course3.reviews.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "comment")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id", updatable = false, nullable = false)
    private Integer commentId;

    @Column(name="comment_body", nullable = false, length = 65535)
    private String commentBody;

    @Column(name="upvote_count")
    private int upvoteCount;

    @Column(name="downvote_count")
    private int downVoteCount;

    @CreatedDate
    @Column(name="create_dt")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name="update_dt")
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

}
