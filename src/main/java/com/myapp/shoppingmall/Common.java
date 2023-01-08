package com.myapp.shoppingmall;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.myapp.shoppingmall.dao.PageRepository;
import com.myapp.shoppingmall.entities.Page;

@ControllerAdvice
public class Common {
    
    @Autowired
    private PageRepository pageRepo;

    @ModelAttribute
    public void sharedData(Model model){
        List<Page> cpages = pageRepo.findAllByOrderBySortingAsc();
        model.addAttribute("cpages", cpages);
    }
}
