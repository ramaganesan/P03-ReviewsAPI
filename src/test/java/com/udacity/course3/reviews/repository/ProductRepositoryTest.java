package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.domain.Product;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@TestPropertySource("classpath:application-test.properties")
@Sql("/productTestLoad.sql")

public class ProductRepositoryTest {




    private final ProductRepository productRepository;

    public ProductRepositoryTest(@Autowired ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Test
    void findProductById(){
        Optional<Product> product = productRepository.findById(1);
        assertThat(product.get().getProductId()).isEqualTo(1);
    }

    @Test
    void saveProduct(){
        Product product = TestUtils.getProduct("saveProductTestName", "saveProductTestdesc", "saveProductTestImage");
        product = productRepository.save(product);
        assertThat(product.getProductId()).isGreaterThan(0);
    }

    @Test
    void updateProduct(){
        String updateProductName = "UpdateProductName";
        Optional<Product> productOptional = productRepository.findById(1);
        Product product = productOptional.orElseThrow(()-> new ResourceNotFoundException("exception"));
        product.setName(updateProductName);
        product = productRepository.save(product);
        assertThat(product.getName()).isEqualToIgnoringCase(updateProductName);
    }

    @Test
    void deleteProduct(){
        Optional<Product> productOptional = productRepository.findById(1);
        Product product = productOptional.orElseThrow(()-> new ResourceNotFoundException("exception"));
        productRepository.delete(product);
        productOptional = productRepository.findById(1);
        assertThat(productOptional.isPresent()).isEqualTo(false);
    }

    @Test()
    void resourceNotFoundException(){
        String exceptionMessage = "No Production Id Found";
        Optional<Product> productOptional = productRepository.findById(10);
        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productOptional.orElseThrow(()-> new ResourceNotFoundException(exceptionMessage));
        });
        assertThat(exception.getMessage()).isEqualToIgnoringCase(exceptionMessage);
    }

}
