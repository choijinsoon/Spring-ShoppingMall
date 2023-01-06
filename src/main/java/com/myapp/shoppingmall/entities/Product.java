package com.myapp.shoppingmall.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name="products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "품명을 입력해 주세요.")
    @Size(min = 2, message = "2자 이상")
    private String name;

    private String slug;

    @NotBlank(message = "상품설명을 입력해 주세요.")
    @Size(min = 2, message = "2자 이상")
    private String description;
    private String image;

    @Pattern(regexp = "^[1-9][0-9]*")
    private String price;

    @Pattern(regexp = "^[1-9][0-9]*", message = "카테고리를 선택해 주세요.")
    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "create_at")
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private LocalDateTime updateAt;
}
