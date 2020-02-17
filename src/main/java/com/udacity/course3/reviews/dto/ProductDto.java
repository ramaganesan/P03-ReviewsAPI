package com.udacity.course3.reviews.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Integer productId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Description is mandatory")
    private String description;

    private String image;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private Collection<ReviewObjectDto> reviewObjectDtos;


}
