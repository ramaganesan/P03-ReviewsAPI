package com.udacity.course3.reviews.document;

import com.udacity.course3.reviews.domain.RatingsEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "reviews")
@CompoundIndexes({
        @CompoundIndex(name = "productId_1_reviewRating_1", def = "{'productId' : 1, 'reviewRating': 1}")
})
public class ReviewDocument {

    @Id
    private ObjectId _id;

    @Indexed(name = "productId_1")
    private int productId;

    @Indexed(name = "reviewRating_1")

    private RatingsEnum reviewRating;

    private String name;

    private String reviewBody;

    private String author;

    private String publisher;

    @Field("comments")
    private List<CommentDocument> commentDocuments;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    public void addComment(CommentDocument commentDocument){
        if(this.commentDocuments == null)
            this.commentDocuments = new ArrayList<>();
        this.commentDocuments.add(commentDocument);
    }
}
