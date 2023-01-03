package com.myapp.shoppingmall.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "pages")
@Data //lombok에 의해 get,set 생성자, toString 자동 생성
public class Page {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "제목을 입력해주세요")
    @Size(min = 2, message = "제목은 2자 이상")
    private String title;
    private String slug;

    @Size(min = 5, message = "내용은 5자 이상")
    private String content;
    private int sorting;
}
