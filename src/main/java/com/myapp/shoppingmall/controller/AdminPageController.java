package com.myapp.shoppingmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myapp.shoppingmall.dao.PageRepository;
import com.myapp.shoppingmall.entities.Page;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/admin/pages")
public class AdminPageController {
    
    @Autowired
    private PageRepository pageRepo;

    @GetMapping
    public String index(Model model){
        List<Page> pages = pageRepo.findAll();
        model.addAttribute("pages", pages);
        return "admin/pages/index";
    }

    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("page", new Page());
        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String add(@Valid Page page, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "admin/pages/add";
        return "redirect:admin/pages/add";
    }
}
