package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.configuration.JPAConfiguration;
import com.udacity.course3.reviews.domain.Comment;
import com.udacity.course3.reviews.domain.Review;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@TestPropertySource("classpath:application-test.properties")
@Sql("/productTestLoad.sql")
@ContextConfiguration(classes = {JPAConfiguration.class})
public class CommentsRepositoryTests {

    private final ReviewRepository reviewRepository;

    private final CommentsRepository commentsRepository;

    public CommentsRepositoryTests(@Autowired ReviewRepository reviewRepository, @Autowired CommentsRepository commentsRepository){
        this.reviewRepository = reviewRepository;
        this.commentsRepository = commentsRepository;
    }

    @Test
    void getCommentsById(){
        Optional<Comment> commentOptional = commentsRepository.findById(1);
        assertThat(commentOptional.get().getCommentId()).isEqualTo(1);
        assertThat(commentOptional.get().getReview()).isNotNull();
    }

    @Test
    void saveComment(){
        String commentBody = "comment_body5";
        Review review = reviewRepository.findById(1).orElse(null);
        assertThat(review).isNotNull();
        Comment comment = TestUtils.buildComment(commentBody,1,0,review);
        review.addComment(comment);
        comment = commentsRepository.save(comment);
        review = reviewRepository.findById(1).orElse(null);
        assertThat(review).isNotNull();
        List<Comment> comments = review.getComments();
        Comment saveComment = comments.stream().filter(comment1 -> comment1.getCommentBody().equalsIgnoreCase(commentBody)).findAny().orElse(null);
        assertThat(saveComment).isNotNull();


    }

    @Test
    void updateComment(){
        String updatedCommentBody="UpdatingCommentBody";
        Comment comment = commentsRepository.findById(1).orElse(null);
        assertThat(comment).isNotNull();
        comment.setCommentBody(updatedCommentBody);
        comment = commentsRepository.save(comment);
        assertThat(comment.getCommentBody()).isEqualToIgnoringCase(updatedCommentBody);
    }

    @Test
    void deleteComment(){
        Comment comment = commentsRepository.findById(1).orElse(null);
        assertThat(comment).isNotNull();
        commentsRepository.delete(comment);
        comment = commentsRepository.findById(1).orElse(null);
        assertThat(comment).isNull();
    }

    @Test
    void findCommentsByReviewId(){
        Collection<Comment> comments = commentsRepository.findByReviewReviewId(1);
        assertThat(comments.size()).isGreaterThan(0);
    }

    @Test()
    void resourceNotFoundException(){
        String exceptionMessage = "Comment Not Found";
        Optional<Comment> commentOptional = commentsRepository.findById(100);
        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            commentOptional.orElseThrow(()-> new ResourceNotFoundException(exceptionMessage));
        });
        assertThat(exception.getMessage()).isEqualToIgnoringCase(exceptionMessage);
    }

}
