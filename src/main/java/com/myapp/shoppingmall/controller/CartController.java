package com.myapp.shoppingmall.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myapp.shoppingmall.dao.ProductRepository;
import com.myapp.shoppingmall.entities.Cart;
import com.myapp.shoppingmall.entities.Product;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
@SuppressWarnings("unchecked")
public class CartController {
    
    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/add/{id}")
    public String add(@PathVariable int id, HttpSession session, Model model){
        Product product = productRepo.getById(id);

        if(session.getAttribute("cart") == null){
            HashMap<Integer, Cart> cart = new HashMap<>();
            cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
            session.setAttribute("cart", cart);
        } else{
            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");
            if(cart.containsKey(id)){
                int qty = cart.get(id).getQuantity();
                cart.put(id, new Cart(id, product.getName(), product.getPrice(), qty++, product.getImage()));
            } else {
                cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
                session.setAttribute("cart", cart);
            }
        }

        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");

        int size = 0;
        int total = 0;

        for(Cart item : cart.values()){
            size += item.getQuantity();
            total += item.getQuantity() * Integer.parseInt(item.getPrice());
        }

        model.addAttribute("size", size);
        model.addAttribute("total", total);

        return "cart_view";
    }

    @GetMapping("/view")
    public String view(HttpSession session, Model model){
        if(session.getAttribute("cart") == null)
            return "redirect:/";

        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");

        model.addAttribute("cart", cart);
        model.addAttribute("noCartView", true);

        return "cart";
    }
}
