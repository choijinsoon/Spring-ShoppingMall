package com.myapp.shoppingmall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.shoppingmall.entities.Page;

public interface PageRepository extends JpaRepository<Page, Integer>{
    Page findBySlug(String slug);
    Page findBySlugAndIdNot(String slug, int id);
    List<Page> findAllByOrderBySortingAsc();
}
