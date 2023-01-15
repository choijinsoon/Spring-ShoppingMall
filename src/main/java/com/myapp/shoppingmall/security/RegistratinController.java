package com.myapp.shoppingmall.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myapp.shoppingmall.dao.UserRepository;
import com.myapp.shoppingmall.entities.User;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistratinController {
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String register(User user){
        return "register";
    }

    @PostMapping
    public String register(@Valid User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors())
            return "register";

        if(!user.getPassword().equals(user.getConfirmPassword())){
            model.addAttribute("passwordNotMatch", "패스워드 확인이 틀림");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        return "redirect:/login";
    }
}
