package com.myapp.shoppingmall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.shoppingmall.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

    Product findByName(String name);

    Product findBySlugAndIdNot(String slug, int id);
    
}