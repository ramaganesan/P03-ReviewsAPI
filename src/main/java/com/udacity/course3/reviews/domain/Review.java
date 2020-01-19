package com.udacity.course3.reviews.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Table(name = "review")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id" , updatable = false, nullable = false)
    private Integer reviewId;

    @Column(name="review_rating", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private RatingsEnum reviewRating;

    @Column(name = "name", nullable = false )
    private String name;

    @Column(name = "review_body", nullable = true, length = 65535 )
    private String reviewBody;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "publisher", nullable = true )
    private String publisher;

    @CreatedDate
    @Column(name="create_dt")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name="update_dt")
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "review" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setReview(this);
    }
}
