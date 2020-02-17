package com.udacity.course3.reviews.mongorepository;

import com.mongodb.WriteResult;
import com.mongodb.client.result.UpdateResult;
import com.udacity.course3.reviews.document.CommentDocument;
import com.udacity.course3.reviews.document.ReviewDocument;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;

public class ReviewRepositoryCommentImpl implements ReviewRepositoryComment {

    private final MongoTemplate mongoTemplate;

    public  ReviewRepositoryCommentImpl (@Autowired MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public long updateCommentDocument(CommentDocument commentDocument) {
        Query query = new Query(Criteria.where("comments._id").is(commentDocument.get_id()));
        Update update = new Update();
        update.set("comments.$.upvoteCount",commentDocument.getUpvoteCount());
        update.set("comments.$.downVoteCount",commentDocument.getDownVoteCount());
        update.set("comments.$.updateDate", LocalDateTime.now());
        UpdateResult result = mongoTemplate.updateFirst(query,update, ReviewDocument.class);
        return result.getModifiedCount();
    }

    @Override
    public CommentDocument addCommentToReview(CommentDocument commentDocument, ObjectId reviewId) {
        Query query = new Query(Criteria.where("_id").is(reviewId));
        ReviewDocument reviewDocument = mongoTemplate.findOne(query,ReviewDocument.class);
        if(reviewDocument != null){
            reviewDocument.addComment(commentDocument);
            mongoTemplate.save(reviewDocument);
        }
        return commentDocument;
    }


}
