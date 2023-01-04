package com.myapp.shoppingmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<Page> pages = pageRepo.findAllByOrderBySortingAsc();
        model.addAttribute("pages", pages);
        return "admin/pages/index";
    }

    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("page", new Page());
        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String add(@Valid Page page, BindingResult bindingResult, RedirectAttributes attr){
        if(bindingResult.hasErrors())
            return "admin/pages/add";
        attr.addFlashAttribute("message", "성공적으로 페이지 추가됨");
        attr.addFlashAttribute("alertClass", "alert-success");

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug();
        Page slugExist = pageRepo.findBySlug(slug);

        if(slugExist != null){
            attr.addFlashAttribute("message", "입력한 slug가 이미 존재합니다.");
            attr.addFlashAttribute("alertClass", "alert-danger");
            attr.addFlashAttribute("page", page);
        } else {
            page.setSlug(slug);
            page.setSorting(100);

            pageRepo.save(page);
        }
        return "redirect:/admin/pages/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        Page page = pageRepo.getById(id);
        model.addAttribute("page", page);
        return "admin/pages/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes attr){
        if(bindingResult.hasErrors())
            return "admin/pages/edit";

        attr.addFlashAttribute("message", "성공적으로 수정됨");
        attr.addFlashAttribute("alertClass", "alert-success");

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug();
        Page slugExist = pageRepo.findBySlugAndIdNot(slug, page.getId());

        if(slugExist != null){
            attr.addFlashAttribute("message", "입력한 slug가 이미 존재합니다.");
            attr.addFlashAttribute("alertClass", "alert-danger");
            attr.addFlashAttribute("page", page);
        } else {
            page.setSlug(slug);
            page.setSorting(100);

            pageRepo.save(page);
        }

        return "redirect:/admin/pages/edit/" + page.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes attr){
        pageRepo.deleteById(id);

        attr.addFlashAttribute("message", "성공적으로 삭제되었습니다.");
        attr.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/pages";
    }

    @PostMapping("/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int[] id){
        int count = 1;
        Page page;

        for(int pageId : id){
            page = pageRepo.getById(pageId);
            page.setSorting(count);

            pageRepo.save(page);
            count++;
        }
        return "ok";
    }
}
