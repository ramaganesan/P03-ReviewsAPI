package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.domain.Comment;
import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.domain.RatingsEnum;
import com.udacity.course3.reviews.domain.Review;
import com.udacity.course3.reviews.dto.ReviewDto;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@TestPropertySource("classpath:application-test.properties")
@Sql("/productTestLoad.sql")
public class ReviewRepositoryTest {

  private final ProductRepository productRepository;

  private final ReviewRepository reviewRepository;

  public ReviewRepositoryTest(@Autowired ProductRepository productRepository, @Autowired ReviewRepository reviewRepository){
      this.productRepository = productRepository;
      this.reviewRepository = reviewRepository;
  }

  @Test
  void getReviewById(){
      Optional<Review> optionalReview = reviewRepository.findById(1);
      assertThat(optionalReview.get().getReviewId()).isEqualTo(1);
      List<Comment> comments = optionalReview.get().getComments();
      assertThat(comments.size()).isGreaterThan(0);
  }
  @Test
  void saveReview(){
      Product product = productRepository.findById(1).get();
      Review review = getReview(RatingsEnum.GOOD,"good review","good review", "john", "amazon", product);
      review = reviewRepository.save(review);
      assertThat(review.getReviewId()).isNotZero();
  }
  @Test
  void updateReview(){
      String updatedReviewName = "ReviewNameUpdated";
      String testCommentBody = "Review1Comment";
      Optional<Review> optionalReview = reviewRepository.findById(1);
      Review updateReview = optionalReview.orElseThrow(() -> new ResourceNotFoundException("Review Not found"));
      updateReview.setName(updatedReviewName);
      Comment comment = Comment.builder().commentBody(testCommentBody).upvoteCount(1).downVoteCount(0).build();
      updateReview.addComment(comment);
      updateReview = reviewRepository.save(updateReview);
      assertThat(updateReview.getName()).isEqualTo(updatedReviewName);
      Comment addedComment = updateReview.getComments().stream().filter(comment1 -> comment1.getCommentBody().equalsIgnoreCase(testCommentBody)).findAny().orElse(null);
      assertThat(addedComment).isNotNull();

  }

  void deleteReview(){
      Optional<Review> optionalReview = reviewRepository.findById(1);
      Review deleteReview = optionalReview.orElseThrow(() -> new ResourceNotFoundException("Review Not found"));
      reviewRepository.delete(deleteReview);
      optionalReview = reviewRepository.findById(1);
      assertThat(optionalReview.isPresent()).isEqualTo(false);
  }

    @Test()
    void resourceNotFoundException(){
        String exceptionMessage = "No Review Id Found";
        Optional<Review> optionalReview = reviewRepository.findById(10);
        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            optionalReview.orElseThrow(()-> new ResourceNotFoundException(exceptionMessage));
        });
        assertThat(exception.getMessage()).isEqualToIgnoringCase(exceptionMessage);
    }

    @Test
    void findReviewViaProduct(){
      Collection<Review> reviews = reviewRepository.findByProductProductId(1);
        assertThat(reviews.size()).isGreaterThan(0);
    }

  public static Review getReview(RatingsEnum ratingsEnum, String reviewName, String reviewBody, String author, String publisher, Product product){

      Review review = Review.builder().reviewRating(ratingsEnum).name(reviewName).reviewBody(reviewBody).author(author).publisher(publisher).product(product).build();
      return review;
  }

}
