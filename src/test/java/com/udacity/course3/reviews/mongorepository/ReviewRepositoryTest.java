package com.udacity.course3.reviews.mongorepository;

import com.mongodb.MongoWriteException;
import com.udacity.course3.reviews.document.CommentDocument;
import com.udacity.course3.reviews.document.ReviewDocument;
import com.udacity.course3.reviews.domain.RatingsEnum;
import com.udacity.course3.reviews.utils.TestUtils;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class ReviewRepositoryTest {

    private final ReviewRepository mongoReviewRepository;

    public ReviewRepositoryTest(@Autowired ReviewRepository mongoReviewRepository){
        this.mongoReviewRepository = mongoReviewRepository;
    }


    void init(){
        ReviewDocument reviewDocument = TestUtils.createReviewDocument(1,RatingsEnum.GOOD,"UdacityMangoConditionerPlusShampooGoodreview","Good product"
                                                            ,"self","udacity");
        mongoReviewRepository.save(reviewDocument);
    }



    @Test
    void saveReviewDocument(){
        ReviewDocument reviewDocument = TestUtils.createReviewDocument(1,RatingsEnum.EXCELLENT,"UdacityMangoConditionerPlusShampooExcellentReview","Excellent product"
                ,"self","udacity");
        reviewDocument = mongoReviewRepository.save(reviewDocument);
        assertThat(reviewDocument.get_id()).isNotNull();
    }

    @Test
    void findReviewByProductId(){
        Pageable pageable = PageRequest.of(0,10);
        Integer productId = 1;
        ReviewDocument reviewDocument = TestUtils.createReviewDocument(productId,RatingsEnum.EXCELLENT,"UdacityMangoConditionerPlusShampooExcellentReview","Excellent product"
                ,"self","udacity");
        reviewDocument = mongoReviewRepository.save(reviewDocument);
        List<ReviewDocument> reviewDocuments = mongoReviewRepository.findReviewDocumentByProductId(productId,pageable);
        assertThat(reviewDocuments.size()).isGreaterThan(1);

    }

    @Test
    void findReviewByReviewId(){
        Integer productId = 1;
        ReviewDocument reviewDocument = TestUtils.createReviewDocument(productId,RatingsEnum.EXCELLENT,"UdacityMangoConditionerPlusShampooExcellentReview","Excellent product"
                ,"self","udacity");
        reviewDocument = mongoReviewRepository.save(reviewDocument);
        Optional<ReviewDocument> optionalReviewDocument = mongoReviewRepository.findReviewDocumentByReviewId(reviewDocument.getReviewId());
        reviewDocument = optionalReviewDocument.get();
        assertThat(reviewDocument).isNotNull();
    }

    @Test()
    void testUniqueReviewIds() {
        Integer productId = 1;
        Integer reviewInteger = 1;
        ReviewDocument reviewDocument = TestUtils.createReviewDocument(productId,RatingsEnum.EXCELLENT,"UdacityMangoConditionerPlusShampooExcellentReview","Excellent product"
                ,"self","udacity");
        ReviewDocument reviewDocument1 = TestUtils.createReviewDocument(productId,RatingsEnum.EXCELLENT,"UdacityMangoConditionerPlusShampooExcellentReview","Excellent product"
                ,"self","udacity");
        reviewDocument1.setReviewId(reviewDocument.getReviewId());
        mongoReviewRepository.save(reviewDocument);
        DuplicateKeyException duplicateKeyException = Assertions.assertThrows(DuplicateKeyException.class, () -> {
            mongoReviewRepository.save(reviewDocument1);
        });

    }

    @Test
    void saveReviewWithComments(){
        ReviewDocument reviewDocument = TestUtils.createReviewDocument(1,RatingsEnum.EXCELLENT,"UdacityMangoConditionerPlusShampooExcellentReview","Excellent product"
                ,"self","udacity");
        CommentDocument commentDocument = TestUtils.createCommentsForReview("good review brother", 0, 0,1);
        reviewDocument.addComment(commentDocument);
        reviewDocument = mongoReviewRepository.save(reviewDocument);
        Optional<ReviewDocument> optionalReviewDocument = mongoReviewRepository.findById(reviewDocument.get_id());
        assertThat(optionalReviewDocument.isPresent()).isTrue();
        assertThat(optionalReviewDocument.get().getCommentDocuments().size()).isGreaterThan(0);
        optionalReviewDocument.get().getCommentDocuments().forEach(commentDocument1 -> {
            assertThat(commentDocument1.get_id().toString()).isNotNull();
        });
    }

    @Test
    void updateCommentsUpVoteCount(){
        ReviewDocument reviewDocument = TestUtils.createReviewDocument(1,RatingsEnum.EXCELLENT,"UdacityMangoConditionerPlusShampooExcellentReview","Excellent product"
                ,"self","udacity");
        CommentDocument commentDocument = TestUtils.createCommentsForReview("good review brother", 0, 0,1);
        reviewDocument.addComment(commentDocument);
        reviewDocument = mongoReviewRepository.save(reviewDocument);
        Optional<ReviewDocument> optionalReviewDocument = mongoReviewRepository.findById(reviewDocument.get_id());
        assertThat(optionalReviewDocument.isPresent()).isTrue();
        assertThat(optionalReviewDocument.get().getCommentDocuments().size()).isGreaterThan(0);
        commentDocument = optionalReviewDocument.get().getCommentDocuments().get(0);
        assertThat(commentDocument.get_id().toString()).isNotNull();
        commentDocument.setUpvoteCount(commentDocument.getUpvoteCount() + 1);
        long updateCount = mongoReviewRepository.updateCommentDocumentUpVote(commentDocument);
        assertThat(updateCount).isEqualTo(1l);


    }


}
