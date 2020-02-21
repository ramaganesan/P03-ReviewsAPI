package com.udacity.course3.reviews.mongorepository;

import com.udacity.course3.reviews.document.CommentDocument;
import com.udacity.course3.reviews.document.ReviewDocument;
import org.bson.types.ObjectId;

public interface ReviewRepositoryComment {

    public long updateCommentDocumentUpVote(CommentDocument commentDocument);

    public long updateCommentDocumentDownVote(CommentDocument commentDocument);

    public ReviewDocument addCommentToReview(CommentDocument commentDocument, Integer reviewId);
}
