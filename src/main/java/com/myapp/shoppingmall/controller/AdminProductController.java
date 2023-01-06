package com.myapp.shoppingmall.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myapp.shoppingmall.dao.CategoryRepository;
import com.myapp.shoppingmall.dao.ProductRepository;
import com.myapp.shoppingmall.entities.Category;
import com.myapp.shoppingmall.entities.Product;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;
    
    @GetMapping()
    public String index(Model model){
        List<Product> products = productRepo.findAll();
        List<Category> categories = categoryRepo.findAll();
        
        HashMap<Integer, String> cateIdAndName = new HashMap<>();
        for(Category category : categories)
            cateIdAndName.put(category.getId(), category.getName());
        
        model.addAttribute("products", products);
        model.addAttribute("cateIdAndName", cateIdAndName);
        
        return "admin/products/index";
    }
    
    @GetMapping("/add")
    public String add(@ModelAttribute Product product, Model model){
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);

        return "admin/products/add";
    }

    @PostMapping("/add") 
    public String add(@Valid Product product, BindingResult bindingResult, @RequestParam(value = "file", required = false) MultipartFile file,
                                            RedirectAttributes attr, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryRepo.findAll();
            model.addAttribute("categories", categories);
            return "admin/products/add";	
        }
        
        boolean fileOk = false;
        byte[] bytes = file.getBytes();	
        String fileName = file.getOriginalFilename();	
        Path path = Paths.get("src/main/resources/static/media/" + fileName); 
        
        if (fileName.endsWith("jpg") || fileName.endsWith("png")) 
            fileOk = true;	
        

        attr.addFlashAttribute("message", "상품이 성공적으로 추가됨!");
        attr.addFlashAttribute("alertClass", "alert-success");
        
        String slug = product.getName().toLowerCase().replace(" ", "-");
        
        Product productExists = productRepo.findByName(product.getName());
        
        if(!fileOk) {	
            attr.addFlashAttribute("message", "이미지는 jpg나 png파일을 사용해주세요!");
            attr.addFlashAttribute("alertClass", "alert-danger");
            attr.addFlashAttribute("product", product);
            
        } else if (productExists != null) {	// 동일한 상품명이 DB에 존재
            attr.addFlashAttribute("message", "이미 존재하는 상품명입니다.");
            attr.addFlashAttribute("alertClass", "alert-danger");
            attr.addFlashAttribute("product", product);
            
        } else {	
            product.setSlug(slug);
            product.setImage(fileName);
            productRepo.save(product);
            
            Files.write(path, bytes);	
        }
        return "redirect:/admin/products/add";
}
}
