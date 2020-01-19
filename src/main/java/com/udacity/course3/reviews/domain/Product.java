package com.udacity.course3.reviews.domain;

import com.udacity.course3.reviews.dto.ReviewDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Builder
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", updatable = false, nullable = false)
    private Integer productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image", nullable = true)
    private String image;

    @CreatedDate
    @Column(name="create_dt")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name="update_dt")
    private LocalDateTime updateDate;


}
