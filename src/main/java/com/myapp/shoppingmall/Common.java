package com.myapp.shoppingmall;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.myapp.shoppingmall.dao.CategoryRepository;
import com.myapp.shoppingmall.dao.PageRepository;
import com.myapp.shoppingmall.entities.Category;
import com.myapp.shoppingmall.entities.Page;

@ControllerAdvice
public class Common {
    
    @Autowired
    private PageRepository pageRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @ModelAttribute
    public void sharedData(Model model){
        List<Page> cpages = pageRepo.findAllByOrderBySortingAsc();
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("cpages", cpages);
        model.addAttribute("ccategories", categories);
    }
}
