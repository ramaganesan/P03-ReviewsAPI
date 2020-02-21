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
    public long updateCommentDocumentUpVote(CommentDocument commentDocument) {
        return createQuery(commentDocument, "comments.$.upvoteCount", commentDocument.getUpvoteCount());
    }

    @Override
    public long updateCommentDocumentDownVote(CommentDocument commentDocument) {
        return createQuery(commentDocument, "comments.$.downVoteCount", commentDocument.getDownVoteCount());
    }

    private long createQuery(CommentDocument commentDocument, String s, int downVoteCount) {
        Query query = new Query(Criteria.where("comments.commentId").is(commentDocument.getCommentId()));
        Update update = new Update();
        update.inc(s, downVoteCount);
        update.set("comments.$.updateDate", LocalDateTime.now());
        UpdateResult result = mongoTemplate.updateFirst(query,update, ReviewDocument.class);
        return result.getModifiedCount();
    }

    @Override
    public ReviewDocument addCommentToReview(CommentDocument commentDocument, Integer reviewId) {
        Query query = new Query(Criteria.where("reviewId").is(reviewId));
        ReviewDocument reviewDocument = mongoTemplate.findOne(query,ReviewDocument.class);
        if(reviewDocument != null){
            reviewDocument.addComment(commentDocument);
            reviewDocument = mongoTemplate.save(reviewDocument);
        }
        return reviewDocument;
    }


}
