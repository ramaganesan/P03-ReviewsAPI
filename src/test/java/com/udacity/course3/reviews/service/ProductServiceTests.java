package com.udacity.course3.reviews.service;

import com.udacity.course3.reviews.configuration.JPAConfiguration;
import com.udacity.course3.reviews.configuration.MongoDBConfiguration;
import com.udacity.course3.reviews.document.ReviewDocument;
import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.domain.RatingsEnum;
import com.udacity.course3.reviews.domain.Review;
import com.udacity.course3.reviews.dto.ReviewDto;
import com.udacity.course3.reviews.dto.ReviewObjectDto;
import com.udacity.course3.reviews.mongorepository.ReviewRepository;
import com.udacity.course3.reviews.repository.CommentsRepository;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.utils.TestUtils;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)

public class ProductServiceTests {

    @MockBean
    private  ProductRepository productRepository;

    @MockBean
    private  ReviewRepository mongoReviewRepository;

    @MockBean
    private  com.udacity.course3.reviews.repository.ReviewRepository reviewRepository;

    private ProductService productService;

    @BeforeEach
    void initProductService(){
        this.productService = new ProductService(productRepository,mongoReviewRepository,new ModelMapper());
    }


    /*void init(){

        Product product = TestUtils.getProduct("saveProductTestName", "saveProductTestdesc", "saveProductTestImage");
        product = productRepository.save(product);

        ReviewDocument reviewDocument = TestUtils.createReviewDocument(1, RatingsEnum.GOOD,"UdacityMangoConditionerPlusShampooGoodreview","Good product"
                ,"self","udacity");
        mongoReviewRepository.save(reviewDocument);

        Review review = TestUtils.getReview(RatingsEnum.GOOD,"good review","good review", "john", "amazon", product);
        reviewRepository.save(review);
    }*/

    @Test
    void testCreateProduct(){
        Product product = TestUtils.getProduct("saveProductTestName1", "saveProductTestdesc1", "saveProductTestImage1");
        product.setProductId(1);
        product.setCreateDate(LocalDateTime.now());
        Mockito.when(productRepository.save(product)).thenReturn(product);
        product = productService.createProduct(product);
        assertThat(product.getProductId()).isNotNull();
        assertThat(product.getCreateDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void testFindById(){
        Product product = TestUtils.getProduct("saveProductTestName1", "saveProductTestdesc1", "saveProductTestImage1");
        product.setProductId(1);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        product = productService.createProduct(product);
        Mockito.when(productRepository.findById(product.getProductId())).thenReturn(java.util.Optional.of(product));
        product = productService.findById(product.getProductId());
        assertThat(product).isNotNull();
    }

    @Test
    void testGetReviewsForProduct(){
        List<Product> products = new ArrayList<>();
        Product product = TestUtils.getProduct("saveProductTestName1", "saveProductTestdesc1", "saveProductTestImage1");
        product.setProductId(1);
        products.add(product);
        product = TestUtils.getProduct("saveProductTestName2", "saveProductTestdesc2", "saveProductTestImage2");
        product.setProductId(2);
        products.add(product);
        Mockito.when(productRepository.findAll()).thenReturn(products);
        List<Product> productsFromService = productService.listProducts();
        assertThat(productsFromService.size()).isGreaterThan(0);
        product = products.get(0);
        Pageable pageable = PageRequest.of(0,10);
        ReviewDocument reviewDocument = TestUtils.createReviewDocument(1, RatingsEnum.GOOD,"UdacityMangoConditionerPlusShampooGoodreview","Good product"
                ,"self","udacity");
        reviewDocument.set_id(new ObjectId());
        List<ReviewDocument> reviewDocuments = new ArrayList<>();
        reviewDocuments.add(reviewDocument);

        List<ReviewDto> reviews = new ArrayList<>();

        Mockito.when(mongoReviewRepository.findReviewDocumentByProductId(product.getProductId(),pageable)).thenReturn(reviewDocuments);
        Mockito.when(productRepository.getReviewsForProduct(product.getProductId(),pageable)).thenReturn(reviews);
        Collection<ReviewObjectDto> reviewObjectDtos = productService.getReviewsForProduct(product.getProductId(),0,10);
        assertThat(reviewObjectDtos).isNotNull();
        assertThat(reviewObjectDtos.size()).isGreaterThan(0);
        List<ReviewObjectDto> reviewObjectDtosGoodRating = reviewObjectDtos.stream().filter(reviewObjectDto -> reviewObjectDto.getReviewRating().equals(RatingsEnum.GOOD)).collect(Collectors.toList());
        assertThat(reviewObjectDtosGoodRating).isNotNull().isNotEmpty();
    }
}

