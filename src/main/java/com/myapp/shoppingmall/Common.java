package com.myapp.shoppingmall;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.myapp.shoppingmall.dao.CategoryRepository;
import com.myapp.shoppingmall.dao.PageRepository;
import com.myapp.shoppingmall.entities.Cart;
import com.myapp.shoppingmall.entities.Category;
import com.myapp.shoppingmall.entities.Page;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class Common {
    
    @Autowired
    private PageRepository pageRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @ModelAttribute
    public void sharedData(Model model, HttpSession session, Principal principal){
        if(principal != null)
            model.addAttribute("pricipal", principal.getName());

        List<Page> cpages = pageRepo.findAllByOrderBySortingAsc();
        List<Category> categories = categoryRepo.findAllByOrderBySortingAsc();

        boolean cartActive = false;

        if(session.getAttribute("cart") != null){
            @SuppressWarnings("unchecked")
            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");

            int size = 0;
            int total = 0;

            for(Cart item : cart.values()){
                size += item.getQuantity();
                total += item.getQuantity() * Integer.parseInt(item.getPrice());
            }

            model.addAttribute("csize", size);
            model.addAttribute("ctotal", total);
            cartActive = true;
        }

        model.addAttribute("cpages", cpages);
        model.addAttribute("ccategories", categories);
        model.addAttribute("cartActive", cartActive);
    }
}
