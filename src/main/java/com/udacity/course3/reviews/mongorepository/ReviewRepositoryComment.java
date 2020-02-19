package com.udacity.course3.reviews.mongorepository;

import com.udacity.course3.reviews.document.CommentDocument;
import org.bson.types.ObjectId;

public interface ReviewRepositoryComment {

    public long updateCommentDocumentUpVote(CommentDocument commentDocument);

    public long updateCommentDocumentDownVote(CommentDocument commentDocument);

    public CommentDocument addCommentToReview(CommentDocument commentDocument, ObjectId reviewId);
}
