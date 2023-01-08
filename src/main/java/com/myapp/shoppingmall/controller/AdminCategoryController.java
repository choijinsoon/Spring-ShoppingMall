package com.myapp.shoppingmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myapp.shoppingmall.dao.CategoryRepository;
import com.myapp.shoppingmall.entities.Category;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    
    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model){
        List<Category> categories = categoryRepo.findAllByOrderBySortingAsc();
        model.addAttribute("categories", categories);
        return "admin/categories/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute Category category){
        return "admin/categories/add";
    }

    @PostMapping("/add")
    public String add(@Valid Category category, BindingResult bindingResult, RedirectAttributes attr){
        if(bindingResult.hasErrors())
            return "admin/categories/add";

        attr.addFlashAttribute("message", "성공적으로 페이지 추가됨");
        attr.addFlashAttribute("alertClass", "alert-success");

        String slug = category.getName().toLowerCase().replace(" ", "-");
        Category nameExist = categoryRepo.findByName(category.getName());

        if(nameExist != null){
            attr.addFlashAttribute("message", "입력한 category가 존재합니다.");
            attr.addFlashAttribute("alertClass", "alert-danger");
            attr.addFlashAttribute("category", category);
        } else{
            category.setSlug(slug);
            category.setSorting(100);

            categoryRepo.save(category);
        }

        return "redirect:/admin/categories/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        Category category = categoryRepo.getById(id);
        model.addAttribute("category", category);
        return "admin/categories/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Category category, BindingResult bindingResult, RedirectAttributes attr){
        if(bindingResult.hasErrors())
            return "admin/categories/edit";

        attr.addFlashAttribute("message", "성공적으로 category 수정됨");
        attr.addFlashAttribute("alertClass", "alert-success");

        String slug = category.getName().toLowerCase().replace(" ", "-");
        Category nameExist = categoryRepo.findByName(category.getName());

        if(nameExist != null){
            attr.addFlashAttribute("message", "입력한 category가 이미 존재합니다.");
            attr.addFlashAttribute("alertClass", "alert-danger");
            attr.addFlashAttribute("category", category);
        } else {
            category.setSlug(slug);

            categoryRepo.save(category);
        }
        return "redirect:/admin/categories/edit/" + category.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes attr){
        categoryRepo.deleteById(id);

        attr.addFlashAttribute("message", "성공적으로 삭제됨");
        attr.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/categories";
    }

    @PostMapping("/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int[] ids){

        int count = 1;
        Category category;

        for(int categoryId : ids){
            category = categoryRepo.getById(categoryId);
            category.setSorting(count);

            categoryRepo.save(category);
            count++;
        }

        return "ok";
    }
}
